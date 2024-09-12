package ru.tuganov.bot.callbacks.simple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetUsersInstrumentCallBack implements CallBackHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;
    private String figi;

    @Override
    public SendMessage handle(Update update) {
        var callBack = update.getCallbackQuery();
        figi = callBack.getData().substring("figi-".length());
        var instrumentDB = databaseSender.getInstrument(figi);
        var price = instrumentDB.maxPrice() == 0 ? instrumentDB.minPrice(): instrumentDB.maxPrice();
        var instrumentInvestment = investmentSender.getInstrument(figi);
        var message = new SendMessage(String.valueOf(callBack.getMessage().getChatId()),
                                      String.format(Message.instrumentInfo,
                                                    instrumentInvestment.name(),
                                                    price,
                                                    instrumentInvestment.price()));
        addInstrumentMenu(message);
        return message;
    }

    private void addInstrumentMenu(SendMessage message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        //кнопка для редактирования запускает ввод
        InlineKeyboardButton editInstrument = new InlineKeyboardButton();
        editInstrument.setText(Message.changePrice);
        editInstrument.setCallbackData("contextEditInstrument" + figi);

        InlineKeyboardButton deleteInstrument = new InlineKeyboardButton();
        deleteInstrument.setText(Message.deleteInstrument);
        deleteInstrument.setCallbackData("simpleDeleteInstrument" + figi);

        InlineKeyboardButton menuInstrument = new InlineKeyboardButton();
        menuInstrument.setText(Message.backCommand);
        menuInstrument.setCallbackData("menu");

        List<InlineKeyboardButton> buttons = List.of(editInstrument, deleteInstrument);
        List<List<InlineKeyboardButton>> rows = List.of(buttons);
        inlineKeyboardMarkup.setKeyboard(rows);

        message.setReplyMarkup(inlineKeyboardMarkup);
    }
}
