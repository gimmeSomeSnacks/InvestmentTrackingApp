package ru.tuganov.bot.handlers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.messages.GetInstruments;
import ru.tuganov.bot.messages.EditPrice;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    private Map<String, ru.tuganov.bot.messages.MessageHandler> inputHandlers;
    @PostConstruct
    public void init() {
        inputHandlers = Map.of(
                "saveInstrument", new EditPrice(databaseSender),
                "getInstruments", new GetInstruments(investmentSender)
        );
    }

    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var message = update.getMessage();
        var chatId = message.getChatId();
        var context = userContext.get(chatId);
        if (context == null || context.isEmpty()) {
            return new SendMessage(String.valueOf(chatId), Message.unknownCommand);
        } else {
            return inputHandlers.get(context.substring(0, "saveInstrument".length())).handle(update, userContext);
        }
    }
}
