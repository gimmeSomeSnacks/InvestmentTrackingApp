package ru.tuganov.bot.callbacks.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.bot.utils.Metrics;

import java.util.Map;

@Component
@Slf4j
public class SaveNewPriceCallBack implements ContextCallBackHandler{
    @Override
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var callBack = update.getCallbackQuery();
        var data = callBack.getData().substring(Metrics.contextCallBackLength);
        //ss/bs
        userContext.put(callBack.getMessage().getChatId(), "saveInstrument" + data);
        log.info("save new: " + data);
        return new SendMessage(String.valueOf(callBack.getMessage().getChatId()), Message.setPrice);
    }
}
