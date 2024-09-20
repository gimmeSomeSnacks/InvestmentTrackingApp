package ru.tuganov.bot.callbacks.simple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.InstrumentsMarkup;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;
import ru.tuganov.dto.MarkupDataDto;

import java.util.List;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
public class GetInstrumentSimpleCallBack implements SimpleCallBack {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    @Override
    public SendMessage handle(Update update) {
        var callBack = update.getCallbackQuery();

        var data = callBack.getData().substring("simpleGUS".length());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(callBack.getMessage().getChatId()));
        if (data.charAt(0) == 'i') {
            var instrumentId = parseLong(data.substring(1));
            var instrumentDB = databaseSender.getInstrument(instrumentId);
            if (instrumentDB.getFigi().isBlank()) {
                sendMessage.setText(Message.deletedError);
                return sendMessage;
            }
            var instrumentInvestment = investmentSender.getInstrument(instrumentDB.getFigi());
            sendMessage.setText(String.format(Message.instrumentInfo,
                                                instrumentInvestment.name(),
                                                instrumentInvestment.sellPrice(),
                                                instrumentInvestment.buyPrice(),
                                                instrumentDB.getSellPrice(),
                                                instrumentDB.getBuyPrice()));
        } else {
            data = 'f' + data;
            sendMessage.setText(Message.chooseBuyOrSell);
        }

        List<MarkupDataDto> markupDataDtoList = List.of(
                new MarkupDataDto(Message.chooseBuy, "contextSNb" + data),
                new MarkupDataDto(Message.chooseSell, "contextSNs" + data),
                new MarkupDataDto(Message.deleteInstrument, "simpleDIC" + data));
        InstrumentsMarkup.setMenu(sendMessage, markupDataDtoList);
        return sendMessage;
    }
}
