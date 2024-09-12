package ru.tuganov.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tuganov.bot.utils.Message;

import java.util.List;

@Slf4j
@Component
public class MenuCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        var message = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Меню действий");
        log.info(message.getText());
        addMenu(message);
        return message;
    }

    private void addMenu(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton getUsersInstruments = new InlineKeyboardButton();
        getUsersInstruments.setText(Message.instrumentList);
        getUsersInstruments.setCallbackData("simpleGetUsersInstruments");

        InlineKeyboardButton addInstrument = new InlineKeyboardButton();
        addInstrument.setText(Message.addInstrument);
        addInstrument.setCallbackData("contextAddInstrument");

        List<InlineKeyboardButton> buttons = List.of(getUsersInstruments, addInstrument);
        List<List<InlineKeyboardButton>> rows = List.of(buttons);
        inlineKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
