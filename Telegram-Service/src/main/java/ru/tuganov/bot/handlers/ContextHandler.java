package ru.tuganov.bot.handlers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.messages.GetInstruments;
import ru.tuganov.bot.messages.InputHandler;
import ru.tuganov.bot.messages.SaveInstrument;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.Map;

//хендлер сообщений ввода (по сути цена/название акций)
@Component
@RequiredArgsConstructor
@Slf4j
public class ContextHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    private Map<String, InputHandler> inputHandlers;
    @PostConstruct
    public void init() {
        inputHandlers = Map.of(
                "saveInstrument", new SaveInstrument(databaseSender, investmentSender),
                "getInstruments", new GetInstruments(investmentSender)
        );
    }

    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var message = update.getMessage();
        var chatId = message.getChatId();
        var context = userContext.get(chatId);
        log.info("context: {}", context);
        if (context.isEmpty() || context == null) {
            return new SendMessage(String.valueOf(chatId), Message.unknownCommand);
        } else {
            return inputHandlers.get(context).handle(update, userContext);
        }
    }
}
