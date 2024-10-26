package ru.tuganov.bot.callbacks.simple;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public interface SimpleCallBack<T> {
    T handle(Update update) throws IOException;
}
