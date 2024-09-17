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
public class SaveNewCallBack implements ContextCallBackHandler {

    @Override
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var callBack = update.getCallbackQuery();
        var chatId = callBack.getMessage().getChatId();
        var data = callBack.getData();
        //si/bi + figi
        //реакция на редактирование/добавление нового элемента
//        var type = data.substring(Metrics.simpleCallBackLength).substring(0, 2);
        var figi = data.substring(Metrics.simpleCallBackLength);

        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(Message.setPrice);
        log.info("сохранить новую акцию:" + figi);
        userContext.put(chatId, "saveInstrument" + figi);
//        InstrumentsMarkup.setMenu(sendMessage, type, figi);
        return sendMessage;
    }
}
