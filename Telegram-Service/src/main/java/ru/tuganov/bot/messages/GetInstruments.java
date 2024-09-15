package ru.tuganov.bot.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.InstrumentsMarkup;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetInstruments implements InputHandler{
    private final InvestmentSender investmentSender;
    //ввели название акции, требуется отобразить список для точного выбора + обнуление контекста
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var message = update.getMessage();
        var chatId = message.getChatId();
        var instrumentList = investmentSender.getInstruments(message.getText());
        if (instrumentList == null || instrumentList.isEmpty()) {
            return new SendMessage(String.valueOf(chatId), Message.unknownInstrument);
        }
        var sendMessage = new SendMessage(String.valueOf(chatId), Message.chooseInstrument);
        //нам выдает список доступных акций, доступно только фиги, нет никаких id пока что
        InstrumentsMarkup.addInstruments(sendMessage, instrumentList, "contextEI");
        log.info("get instruments from investment!");
        userContext.put(chatId, "saveInstrument");
        return sendMessage;
    }
}
