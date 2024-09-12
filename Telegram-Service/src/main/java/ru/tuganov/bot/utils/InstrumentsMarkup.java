package ru.tuganov.bot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tuganov.dto.InstrumentDto;

import java.util.ArrayList;
import java.util.List;

public class InstrumentsMarkup {
    public static void addInstruments(SendMessage sendMessage, List<InstrumentDto> instruments, String dataText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < instruments.size(); i++) {
            InlineKeyboardButton instrumentButton = new InlineKeyboardButton();
            instrumentButton.setText(instruments.get(i).name());
            instrumentButton.setCallbackData(dataText + instruments.get(i).figi());
            buttons.add(instrumentButton);
            if (i % 5 == 0) {
                rows.add(buttons);
                buttons = new ArrayList<>();
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
