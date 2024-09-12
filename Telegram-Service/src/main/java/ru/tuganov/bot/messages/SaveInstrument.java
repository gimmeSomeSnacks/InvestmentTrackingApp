package ru.tuganov.bot.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;
import ru.tuganov.dto.InstrumentDBDto;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SaveInstrument implements InputHandler{

    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;
    //получаем цену и сохраняем инструмент
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var message = update.getMessage();
        var chatId = message.getChatId();

        var figi = userContext.get(chatId).substring("saveInstrument".length());
        var instrument = investmentSender.getInstrument(figi);

        var usersPrice = Double.parseDouble(message.getText());
        var minPrice = instrument.price() > usersPrice ? instrument.price() : 0;
        var maxPrice = instrument.price() < usersPrice ? instrument.price() : 0;
        var instrumentDBDto = new InstrumentDBDto(chatId, figi, maxPrice, minPrice);
        databaseSender.saveInstrument(instrumentDBDto);
        userContext.put(chatId, "");
        return new SendMessage(String.valueOf(chatId), Message.instrumentSaved);
    }
}
