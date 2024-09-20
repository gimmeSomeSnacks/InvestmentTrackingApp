package ru.tuganov.bot.messages;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public interface MessageHandler {
    SendMessage handle(Update update, Map<Long, String> userContext);
}
