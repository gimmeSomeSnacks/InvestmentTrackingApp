package ru.tuganov.bot.callbacks.context;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;

import java.util.Map;

@Component
public class EditThisInstrumentCallBack implements ContextCallBackHandler {
    @Override
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        //реакция на меню редактирования => ставим контекст изменения price и ждем ввод
        var callBack = update.getCallbackQuery();
        var figi = callBack.getData().substring("contextEditInstrument".length());
        userContext.put(callBack.getMessage().getChatId(), "saveInstrument" + figi);
        return new SendMessage(String.valueOf(update.getCallbackQuery().getMessage().getChatId()),
                Message.setPrice);
    }
}
