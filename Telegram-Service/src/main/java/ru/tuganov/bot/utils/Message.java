package ru.tuganov.bot.utils;

public class Message {
    public static final String startCommand = "Привет! Я бот для отслеживания цен для покупки/продажи активов. Все команды: \nМеню: /menu";
    public static final String unknownCommand = "Я не понял, какую команду вы ввели, повторит еще раз.";
    public static final String instrumentList = "Список акций";
    public static final String instrumentInfo = "Текущая акция %s имеет цену для продажи %g, цену для покупки %g.\n /" +
            "                                    Установлена цена для продажи %g, цена для покупки %g.";
    public static final String changePrice = "Изменить цену";
    public static final String deleteInstrument = "Удалить акцию из списка";
    public static final String instrumentDeleted = "Удалено из списка";
    public static final String instrumentSaved = "Сохранено";
    public static final String chooseBuyOrSell = "Выберите что редактировать:";
    public static final String chooseBuy = "Цена покупки";
    public static final String chooseSell = "Цена продажи";
    public static final String savePrice = "Сохранить";
    public static final String setPrice = "Введите цену:";
    public static final String backCommand = "Назад";
    public static final String addInstrument = "Добавить акцию";
    public static final String inputInstrument = "Напишите акцию:";
    public static final String unknownInstrument = "Такой акции нет на рынке";
    public static final String chooseInstrument = "Выберите акцию из списка";
}
