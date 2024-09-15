package ru.tuganov.bot.handlers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CallBacksHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    private Map<String, CallBackHandler> callBacks;

    @PostConstruct
    private void init() {
        callBacks = Map.of(
                "simpleGUS", new GetUsersInstrumentCallBack(databaseSender, investmentSender),
                "simpleGUI", new GetUsersInstrumentsCallBack(databaseSender, investmentSender),
                "simpleDIC", new DeleteThisInstrumentCallBack(databaseSender)
        );
    }
    //надо как-то контекст обрезать))))
    private Map<String, ContextCallBackHandler> contextCallBackHandler = Map.of (
      "contextAI", new AddInstrumentCallBack(),
      "contextEI", new EditThisInstrumentCallBack()
    );

    public SendMessage handleCallBack(Update update, Map<Long, String> userContext) {
        var callBackData = update.getCallbackQuery().getData();
        if (callBackData.startsWith("context")) {
            return contextCallBackHandler.get(callBackData.substring(0, "contextEI".length())).handle(update, userContext);
        } else {
            log.info(callBackData);
            return callBacks.get(callBackData.substring(0, "simpleGUS".length())).handle(update);
        }
    }
}
