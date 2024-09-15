package ru.tuganov.bot.callbacks.context;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;

import java.util.Map;

@Component
@Slf4j
public class EditThisInstrumentCallBack implements ContextCallBackHandler {
    @Override
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        //реакция на меню редактирования => ставим контекст изменения price и ждем ввод
        //либо мы редактируем, либо мы добавляем новое
        var callBack = update.getCallbackQuery();
        var data = callBack.getData().substring("contextEI".length());
        log.info("save new price for instrument: {}", data);
        userContext.put(callBack.getMessage().getChatId(), "saveInstrument" + data);
        return new SendMessage(String.valueOf(update.getCallbackQuery().getMessage().getChatId()),
                Message.setPrice);
    }
}
