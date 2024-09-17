package ru.tuganov.bot.handlers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.callbacks.context.AddInstrumentCallBack;
import ru.tuganov.bot.callbacks.context.ContextCallBackHandler;
import ru.tuganov.bot.callbacks.context.SaveNewCallBack;
//import ru.tuganov.bot.callbacks.context.SavePriceCallBack;
import ru.tuganov.bot.callbacks.context.SaveNewPriceCallBack;
import ru.tuganov.bot.callbacks.simple.*;
import ru.tuganov.bot.utils.Metrics;
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
//            "contextSP", new SavePriceCallBack(),
            "contextSN", new SaveNewCallBack(),
            "contextSP", new SaveNewPriceCallBack()
    );

    public SendMessage handleCallBack(Update update, Map<Long, String> userContext) {
        var callBackData = update.getCallbackQuery().getData();
        log.info("data: " + callBackData);
//        log.info("type: " + callBackData.substring(0, Metrics.contextCallBackLength));
        if (callBackData.startsWith("context")) {
            return contextCallBackHandler.get(callBackData.substring(0, Metrics.contextCallBackLength)).handle(update, userContext);
        } else {
            return callBacks.get(callBackData.substring(0, Metrics.simpleCallBackLength)).handle(update);
        }
    }
}
