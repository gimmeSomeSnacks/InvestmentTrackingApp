package ru.tuganov.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tuganov.bot.handlers.CallBacksHandler;
import ru.tuganov.bot.handlers.CommandHandler;
import ru.tuganov.bot.handlers.ContextHandler;
import ru.tuganov.dto.InstrumentDBDto;
import ru.tuganov.investment.AchievedInstrument;
import ru.tuganov.investment.InstrumentObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Getter
    @Value("${bot.token}")
    private String botToken;

    private final CommandHandler commandHandler;
    private final CallBacksHandler callBacksHandler;
    private final ContextHandler contextHandler;

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
//            log.info(update.getMessage().getText());
//            log.info(context == null ? "null" : context);
            if (context != null && !context.isBlank()) {
                sendMessage(contextHandler.handle(update, userContext));
            } else {
                sendMessage(commandHandler.handleCommands(update));
            }
        } else if (update.hasCallbackQuery()){
            sendMessage(callBacksHandler.handleCallBack(update, userContext));
        } else {
            log.info("nothing");
        }
        if (!achievedInstruments.isEmpty()) {

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
            log.info(e.getMessage());
        }
    }


    @Scheduled(fixedRateString = "${scheduling.fixed-rate}")
    private void checkAchievedInstruments() {
        if (!achievedInstruments.isEmpty()) {
            for (var instrument : achievedInstruments) {
                var newMessage = new SendMessage(instrument.chatId().toString(), "ТЕКСТ Я ПРИДУМАЮ NEGRO");
                sendMessage(newMessage);
            }
            achievedInstruments.clear();
        }
    }
}
