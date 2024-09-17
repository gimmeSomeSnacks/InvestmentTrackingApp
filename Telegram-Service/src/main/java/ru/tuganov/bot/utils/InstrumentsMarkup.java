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
        for (int i = 0; i < instruments.size(); i++) {
            InlineKeyboardButton instrumentButton = new InlineKeyboardButton();
            instrumentButton.setText(instruments.get(i).name());
            instrumentButton.setCallbackData(dataText + instruments.get(i).figi());
            log.info("list: " + dataText +" " + instruments.get(i).figi());
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

    public static void setMenu(SendMessage sendMessage, String type, long data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        switch (type) {
            case "sf" -> {
                InlineKeyboardButton buyButton = new InlineKeyboardButton();
                buyButton.setText(Message.chooseBuy);
                buyButton.setCallbackData("contextSPbi" + data);
                buttons.add(buyButton);
            }
            case "bf" -> {
                InlineKeyboardButton sellButton = new InlineKeyboardButton();
                sellButton.setText(Message.chooseSell);
                sellButton.setCallbackData("contextSPsi" + data);
                buttons.add(sellButton);
            }
        }

        InlineKeyboardButton saveButton = new InlineKeyboardButton();
        saveButton.setText(Message.changePrice);
        saveButton.setCallbackData(".........." + data);
        buttons.add(saveButton);


        rows.add(buttons);
        inlineKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
