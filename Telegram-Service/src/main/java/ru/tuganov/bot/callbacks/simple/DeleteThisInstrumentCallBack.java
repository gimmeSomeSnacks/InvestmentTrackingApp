package ru.tuganov.bot.callbacks.simple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteThisInstrumentCallBack implements CallBackHandler {
    private final DatabaseSender databaseSender;

    @Override
    public SendMessage handle(Update update) {
        var callBack = update.getCallbackQuery();
        var instrument = callBack.getData();
        var instrumentId = instrument.substring("simpleDIC".length());
        log.info("deleted: {}", instrumentId);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        databaseSender.deleteInstrument(parseLong(instrumentId));
        return new SendMessage(String.valueOf(callBack.getMessage().getChatId()), Message.instrumentDeleted);
    }
}
