package ru.tuganov.bot.callbacks.simple;

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
import ru.tuganov.dto.InstrumentDto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetUsersInstrumentsCallBack implements CallBackHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    //получаем список всех инструментов пользователя
    @Override
    public SendMessage handle(Update update) {
        log.info("список инструментов:");
        var instrumentDBDto = databaseSender.getInstruments(update.getCallbackQuery().getMessage().getChatId());
        var usersInstruments = parserDto(instrumentDBDto);
        var message = new SendMessage(String.valueOf(update.getCallbackQuery().getMessage().getChatId()), Message.instrumentList);
        InstrumentsMarkup.addInstruments(message, usersInstruments, "simpleGUSi");
        return message;
    }

    //меняем формат DTO, чтобы можно было отобразить весь список
    private List<InstrumentDto> parserDto(List<InstrumentDBDto> instrumentDBDtoList) {
        List<InstrumentDto> usersInstruments = new ArrayList<>();
        if (instrumentDBDtoList != null) {
            for (InstrumentDBDto instrumentDBDto : instrumentDBDtoList) {
                var instrumentId = instrumentDBDto.getInstrumentId();
                var instrument = investmentSender.getInstrument(instrumentDBDto.getFigi());
                usersInstruments.add(new InstrumentDto(instrument.name(),
                        instrumentId.toString(),
                        instrumentDBDto.getBuyPrice(),
                        instrumentDBDto.getSellPrice()));
            }
        }
        return usersInstruments;
    }
}
