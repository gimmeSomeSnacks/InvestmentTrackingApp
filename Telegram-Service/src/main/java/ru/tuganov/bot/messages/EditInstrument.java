package ru.tuganov.bot.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.InstrumentsMarkup;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;
import ru.tuganov.dto.InstrumentDBDto;

import java.util.Map;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class EditInstrument implements InputHandler{

    private final DatabaseSender databaseSender;
    //получаем цену и сохраняем инструмент
    public SendMessage handle(Update update, Map<Long, String> userContext) {

        var message = update.getMessage();
        var chatId = message.getChatId();
        log.info("Вводим цену" + message.getText());
        var data = userContext.get(chatId).substring("saveInstrument".length());
        //bf/sf
        //bi/si
        //ss/bs
        var type = data.substring(0, 2); //si
        data = data.substring(2); //figi

        String figi;
        Long instrumentId = 0L;
        InstrumentDBDto instrumentDB;

        double buyPrice = 0;
        double sellPrice = 0;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (type.charAt(1) == 'i') {
            //либо мы редактируем
            instrumentId = parseLong(data);
            instrumentDB = databaseSender.getInstrument(instrumentId);
            figi = instrumentDB.getFigi();
            buyPrice = instrumentDB.getBuyPrice();
            sellPrice = instrumentDB.getSellPrice();

            sendMessage.setText(Message.instrumentSaved);
        } else {
            //либо создаем новую запись
            //обрубаем тип и вхождение, чтобы получить индекс
            figi = data;
            sendMessage.setText(Message.chooseBuyOrSell);
        }

        var usersPrice = Double.parseDouble(message.getText());

        if (type.startsWith("s")){
            sellPrice = usersPrice;
        } else if (type.startsWith("b")){
            buyPrice = usersPrice;
        }
        log.info("prices:" + sellPrice + " " + buyPrice);
        var instrumentDBDto = new InstrumentDBDto(instrumentId, chatId, figi, sellPrice, buyPrice);

        var id = databaseSender.saveInstrument(instrumentDBDto);
        if (type.charAt(1) == 'f') {
            InstrumentsMarkup.setMenu(sendMessage, type, id);
        }
        userContext.put(chatId, "");
        return sendMessage;
    }
}
