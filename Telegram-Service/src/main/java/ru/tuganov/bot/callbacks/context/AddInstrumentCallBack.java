package ru.tuganov.bot.callbacks.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddInstrumentCallBack implements ContextCallBackHandler {
    //нажимаем в menu кнопку add - отзыв на кнопку => ставим контекст на добавление и ждем клаву
    @Override
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var callBack = update.getCallbackQuery();
        log.info("addButton pressed! data: " + callBack.getData());
        var chatId = callBack.getMessage().getChatId();
        userContext.put(chatId, "getInstruments");
        //ставлю контекст для ввода с клавы
        return new SendMessage(String.valueOf(chatId), Message.inputInstrument);
    }
}
