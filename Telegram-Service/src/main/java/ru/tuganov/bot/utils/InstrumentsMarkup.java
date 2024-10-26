package ru.tuganov.bot.utils;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tuganov.dto.InstrumentDto;
import ru.tuganov.dto.MarkupDataDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        rows.add(buttons);
        inlineKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    public static <T> void setMenu(T sendMessage, List<MarkupDataDto> markupDataDtoList) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (MarkupDataDto markupDataDto : markupDataDtoList) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(markupDataDto.text());
            button.setCallbackData(markupDataDto.callBackData());
            buttons.add(button);
        }

        rows.add(buttons);
        inlineKeyboardMarkup.setKeyboard(rows);
        if (sendMessage instanceof SendMessage)
            ((SendMessage) sendMessage).setReplyMarkup(inlineKeyboardMarkup);
        else if (sendMessage instanceof SendPhoto)
            ((SendPhoto) sendMessage).setReplyMarkup(inlineKeyboardMarkup);
    }
}
