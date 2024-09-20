package ru.tuganov.bot.handlers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.callbacks.context.AddInstrumentCallBack;
import ru.tuganov.bot.callbacks.context.ContextCallBackHandler;
import ru.tuganov.bot.callbacks.context.NewPriceCallBack;
import ru.tuganov.bot.callbacks.context.EditPriceCallBack;
import ru.tuganov.bot.callbacks.simple.*;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.bot.utils.Metrics;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CallBackHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    private Map<String, SimpleCallBack> callBacks;

    @PostConstruct
    private void init() {
        callBacks = Map.of(
                "simpleGUS", new GetInstrumentSimpleCallBack(databaseSender, investmentSender),
                "simpleGUI", new GetInstrumentsSimpleCallBack(databaseSender, investmentSender),
                "simpleDIC", new DeleteSimpleCallBack(databaseSender)
        );
    }
    private Map<String, ContextCallBackHandler> contextCallBackHandler = Map.of (
      "contextAI", new AddInstrumentCallBack(),
            "contextSN", new NewPriceCallBack(),
            "contextSP", new EditPriceCallBack()
    );

    public SendMessage handleCallBack(Update update, Map<Long, String> userContext) {
        var callBackData = update.getCallbackQuery().getData();
        if (callBackData.startsWith("context")) {
            userContext.put(update.getCallbackQuery().getMessage().getChatId(), "");
            var callBack = contextCallBackHandler.get(callBackData.substring(0, Metrics.contextCallBackLength));
            if (callBack == null)
                return new SendMessage(String.valueOf(update.getCallbackQuery().getMessage().getChatId()), Message.unknownCommand);
            else
                return callBack.handle(update, userContext);
        } else {
            var callBack = callBacks.get(callBackData.substring(0, Metrics.simpleCallBackLength));
            if (callBack == null)
                return new SendMessage(String.valueOf(update.getCallbackQuery().getMessage().getChatId()), Message.unknownCommand);
            else
                return callBack.handle(update);
        }
    }
}
