package ru.tuganov.bot.callbacks.simple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;

import java.util.List;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetUsersInstrumentCallBack implements CallBackHandler {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

    @Override
    public SendMessage handle(Update update) {
        var callBack = update.getCallbackQuery();
        log.info(callBack.getData());
        var instrumentId = parseLong(callBack.getData().substring("simpleGUSid".length()));
        var instrumentDB = databaseSender.getInstrument(instrumentId);
        log.info("info: {} {} {}", instrumentId, instrumentDB.getInstrumentId(), instrumentDB.getFigi());
        var price = instrumentDB.getMaxPrice() + instrumentDB.getMinPrice();
        var instrumentInvestment = investmentSender.getInstrument(instrumentDB.getFigi());
        var message = new SendMessage(String.valueOf(callBack.getMessage().getChatId()),
                                      String.format(Message.instrumentInfo,
                                                    instrumentInvestment.name(),
                                                    instrumentInvestment.price(),
                                                    price));
        addInstrumentMenu(message, instrumentId);
        return message;
    }

    private void addInstrumentMenu(SendMessage message, Long instrumentId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        //кнопка для редактирования запускает ввод
        InlineKeyboardButton editInstrument = new InlineKeyboardButton();
        editInstrument.setText(Message.changePrice);
        editInstrument.setCallbackData("contextEIid" + instrumentId);

        InlineKeyboardButton deleteInstrument = new InlineKeyboardButton();
        deleteInstrument.setText(Message.deleteInstrument);
        deleteInstrument.setCallbackData("simpleDIC" + instrumentId);

//        InlineKeyboardButton menuInstrument = new InlineKeyboardButton();
//        menuInstrument.setText(Message.backCommand);
//        menuInstrument.setCallbackData("menu");

        List<InlineKeyboardButton> buttons = List.of(editInstrument, deleteInstrument);
        List<List<InlineKeyboardButton>> rows = List.of(buttons);
        inlineKeyboardMarkup.setKeyboard(rows);

        message.setReplyMarkup(inlineKeyboardMarkup);
    }
}
