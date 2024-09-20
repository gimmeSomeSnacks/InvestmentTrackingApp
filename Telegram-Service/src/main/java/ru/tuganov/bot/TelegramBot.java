package ru.tuganov.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tuganov.bot.handlers.CallBackHandler;
import ru.tuganov.bot.handlers.CommandHandler;
import ru.tuganov.bot.handlers.MessageHandler;
import ru.tuganov.investment.AchievedInstrument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Getter
    @Value("${bot.token}")
    private String botToken;

    private final CommandHandler commandHandler;
    private final CallBackHandler callBackHandler;
    private final MessageHandler messageHandler;

    @NonFinal
    private final Map<Long, String> userContext = new HashMap<>();
    @NonFinal
    public final static List<AchievedInstrument> achievedInstruments = new ArrayList<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var chatId = message.getChatId();
            var context = userContext.get(chatId);
            if (context != null && !context.isBlank()) {
                sendMessage(messageHandler.handle(update, userContext));
            }
            if (message.getText().startsWith("/")) {
                sendMessage(commandHandler.handleCommands(update, userContext));
            }
        } else if (update.hasCallbackQuery()){
            sendMessage(callBackHandler.handleCallBack(update, userContext));
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
