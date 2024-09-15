package ru.tuganov.bot.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;
import ru.tuganov.dto.InstrumentDBDto;

import java.util.Map;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveInstrument implements InputHandler{

    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;
    //получаем цену и сохраняем инструмент
    public SendMessage handle(Update update, Map<Long, String> userContext) {
        var message = update.getMessage();
        var chatId = message.getChatId();

        var data = userContext.get(chatId).substring("saveInstrument".length());
        String figi;
        Double minPrice, maxPrice;
        var instrumentId = 0L;
        if (data.startsWith("id")) {
            //либо мы редактируем
            instrumentId = parseLong(data.substring("id".length()));
            var instrumentDB = databaseSender.getInstrument(instrumentId);
            figi = instrumentDB.getFigi();

        } else {
            //либо создаем новую запись
            figi = data;
        }

        var usersPrice = Double.parseDouble(message.getText());
        var instrument = investmentSender.getInstrument(figi);
        minPrice = instrument.price() > usersPrice ? usersPrice : 0;
        maxPrice = instrument.price() < usersPrice ? usersPrice : 0;
        var instrumentDBDto = new InstrumentDBDto(chatId, figi, maxPrice, minPrice, instrumentId);

        log.info("first step{}", instrumentDBDto.getFigi());
        databaseSender.saveInstrument(instrumentDBDto);
        userContext.put(chatId, "");
        return new SendMessage(String.valueOf(chatId), Message.instrumentSaved);
    }
}
