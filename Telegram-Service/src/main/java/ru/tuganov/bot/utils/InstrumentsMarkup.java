package ru.tuganov.bot.utils;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tuganov.dto.InstrumentDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InstrumentsMarkup {
    public static void addInstruments(SendMessage sendMessage, List<InstrumentDto> instruments, String dataText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//        log.info("size: {}", instruments.size());
        for (int i = 0; i < instruments.size(); i++) {
            InlineKeyboardButton instrumentButton = new InlineKeyboardButton();
            instrumentButton.setText(instruments.get(i).name());
            instrumentButton.setCallbackData(dataText + instruments.get(i).figi());
            buttons.add(instrumentButton);
            if (i % 5 == 0) {
                rows.add(buttons);
                buttons = new ArrayList<>();
            }
//            log.info("instrument: {} {}", instruments.get(i).figi(), instruments.get(i).price());
        }
        rows.add(buttons);
//        log.info("rows: {}", rows.size());
        inlineKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
