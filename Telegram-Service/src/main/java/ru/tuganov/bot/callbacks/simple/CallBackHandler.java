package ru.tuganov.bot.callbacks.simple;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallBackHandler {
    SendMessage handle(Update update);
}
