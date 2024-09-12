package ru.tuganov.bot.commands;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.Message;

@RequiredArgsConstructor
public class StartCommand implements Command {
    @Override
    public SendMessage apply(Update update) {
        return new SendMessage(String.valueOf(update.getMessage().getChatId()), Message.startCommand);
    }
}
