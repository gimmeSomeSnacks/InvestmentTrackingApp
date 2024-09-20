package ru.tuganov.bot.utils;

public class Message {
    public static final String startCommand = "Привет! Я бот для отслеживания цен для покупки/продажи активов. Все команды: \nМеню: /menu";
    public static final String unknownCommand = "Я не понял, какую команду вы ввели, повторит еще раз.";
    public static final String instrumentList = "Список акций";
    public static final String instrumentInfo = "Текущая акция %s имеет цену для продажи %.2f, цену для покупки %.2f.\n" +
                                                "Установлена цена для продажи %.2f, цена для покупки %.2f.";
    public static final String deleteInstrument = "Удалить акцию из списка";
    public static final String instrumentDeleted = "Удалено из списка";
    public static final String instrumentSaved = "Сохранено";
    public static final String chooseBuyOrSell = "Выберите что редактировать:";
    public static final String chooseBuy = "Цена покупки";
    public static final String chooseSell = "Цена продажи";
    public static final String setPrice = "Введите цену (для дробных цен вводите через точку, например: 123.45";
    public static final String addInstrument = "Добавить акцию";
    public static final String inputInstrument = "Напишите акцию:";
    public static final String unknownInstrument = "Такой акции нет на рынке";
    public static final String chooseInstrument = "Выберите акцию из списка";
    public static final String noInstruments = "Нет акций.";
    public static final String achievedInstrument = "Акция %s достигла нужной цены!";
    public static final String getInstrument = "Посмотреть акцию";
    public static final String deletedError = "Акции нет в списке.";
}
