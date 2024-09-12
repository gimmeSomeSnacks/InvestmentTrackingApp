package ru.tuganov.bot.handlers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.callbacks.context.AddInstrumentCallBack;
import ru.tuganov.bot.callbacks.context.ContextCallBackHandler;
import ru.tuganov.bot.callbacks.context.EditThisInstrumentCallBack;
import ru.tuganov.bot.callbacks.simple.*;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CallBacksHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    private Map<String, CallBackHandler> callBacks;
    @PostConstruct
    private void init() {
        callBacks = Map.of(
                "simpleGetUsersInstrument", new GetUsersInstrumentCallBack(databaseSender, investmentSender),
                "simpleGetUsersInstruments", new GetUsersInstrumentsCallBack(databaseSender, investmentSender),
                "simpleDeleteInstrument", new DeleteThisInstrumentCallBack(databaseSender)
        );
    }

    private Map<String, ContextCallBackHandler> contextCallBackHandler = Map.of (
      "contextAddInstrument", new AddInstrumentCallBack(),
      "contextEditInstrument", new EditThisInstrumentCallBack()
    );

    public SendMessage handleCallBack(Update update, Map<Long, String> userContext) {
        var callBackData = update.getCallbackQuery().getData();
        if (callBackData.startsWith("context")) {
            return contextCallBackHandler.get(callBackData).handle(update, userContext);
        } else {
            return callBacks.get(callBackData).handle(update);
        }
    }
}
