package ru.tuganov.bot.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.InstrumentsMarkup;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetInstruments implements InputHandler{
    private final InvestmentSender investmentSender;
    //ввели название акции, требуется отобразить список для точного выбора + обнуление контекста
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var message = update.getMessage();
        var chatId = message.getChatId();
        userContext.put(chatId, "");
        var instrumentList = investmentSender.getInstruments(message.getText());

        if (instrumentList == null || instrumentList.isEmpty()) {
            return new SendMessage(String.valueOf(chatId), Message.unknownInstrument);
        }
        var sendMessage = new SendMessage(String.valueOf(chatId), Message.chooseInstrument);
        InstrumentsMarkup.addInstruments(sendMessage, instrumentList, "saveInstrument");
        return sendMessage;
    }
}
