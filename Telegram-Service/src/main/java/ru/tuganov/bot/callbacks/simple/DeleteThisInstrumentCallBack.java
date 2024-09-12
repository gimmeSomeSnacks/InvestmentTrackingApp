package ru.tuganov.bot.callbacks.simple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;

@Component
@RequiredArgsConstructor
public class DeleteThisInstrumentCallBack implements CallBackHandler {
    private final DatabaseSender databaseSender;

    @Override
    public SendMessage handle(Update update) {
        var instrument = update.getCallbackQuery().getData();
        var instrumentFigi = instrument.substring("deleteInstrument-".length());
        databaseSender.deleteInstrument(instrumentFigi);
        return new SendMessage(String.valueOf(update.getMessage().getChatId()), Message.instrumentDeleted);
    }
}
