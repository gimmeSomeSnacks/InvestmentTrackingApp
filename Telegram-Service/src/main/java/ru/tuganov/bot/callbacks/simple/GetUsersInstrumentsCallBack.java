package ru.tuganov.bot.callbacks.simple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.InstrumentsMarkup;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;
import ru.tuganov.dto.InstrumentDBDto;
import ru.tuganov.dto.InstrumentDto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetUsersInstrumentsCallBack implements CallBackHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    @Override
    public SendMessage handle(Update update) {
        var instrumentDBDto = databaseSender.getInstruments(update.getCallbackQuery().getMessage().getChatId());
        var usersInstruments = parserDto(instrumentDBDto);
        var message = new SendMessage(String.valueOf(update.getCallbackQuery().getMessage().getChatId()), Message.instrumentList);
        InstrumentsMarkup.addInstruments(message, usersInstruments, "get-figi-");
        return message;
    }

    private List<InstrumentDto> parserDto(List<InstrumentDBDto> instrumentDBDtoList) {
        List<InstrumentDto> usersInstruments = new ArrayList<>();
        if (instrumentDBDtoList != null) {
            for (InstrumentDBDto instrumentDBDto : instrumentDBDtoList) {
                var figi = instrumentDBDto.figi();
                var instrument = investmentSender.getInstrument(figi);
                usersInstruments.add(new InstrumentDto(instrument.name(), figi, 0));
            }
        }
        return usersInstruments;
    }
}
