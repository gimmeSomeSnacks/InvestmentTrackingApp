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

        var data = callBack.getData().substring("simpleGUS".length());
        log.info("один инструмент: " + data); //figi
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(callBack.getMessage().getChatId()));
        //либо у нас id, либо figi
        if (data.charAt(0) == 'i') {
            var instrumentId = parseLong(data.substring(1));
            var instrumentDB = databaseSender.getInstrument(instrumentId);
//        log.info("info: {} {} {}", instrumentId, instrumentDB.getInstrumentId(), instrumentDB.getFigi());
            var instrumentInvestment = investmentSender.getInstrument(instrumentDB.getFigi());
            sendMessage.setText(String.format(Message.instrumentInfo,
                                                instrumentInvestment.name(),
                                                instrumentInvestment.sellPrice(),
                                                instrumentInvestment.buyPrice(),
                                                instrumentDB.getSellPrice(),
                                                instrumentDB.getBuyPrice()));
        } else {
            data = 'f' + data;
            sendMessage.setText(Message.chooseBuyOrSell);
        }

        addInstrumentMenu(sendMessage, data);
        return sendMessage;
    }

    private void addInstrumentMenu(SendMessage message, String data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //si/bi - sale/buy instrument - тупо редактирование
        //кнопки для изменения фиксации цены покупки, продажи или удаления акции вообще
        InlineKeyboardButton buyButton = new InlineKeyboardButton();
        buyButton.setText(Message.chooseBuy);
        buyButton.setCallbackData("contextSNb" + data);

        InlineKeyboardButton sellButton = new InlineKeyboardButton();
        sellButton.setText(Message.chooseSell);
        sellButton.setCallbackData("contextSNs" + data);


        InlineKeyboardButton deleteInstrument = new InlineKeyboardButton();
        deleteInstrument.setText(Message.deleteInstrument);
        deleteInstrument.setCallbackData("simpleDIC" + data);

        List<InlineKeyboardButton> buttons = List.of(buyButton, sellButton, deleteInstrument);
        List<List<InlineKeyboardButton>> rows = List.of(buttons);
        inlineKeyboardMarkup.setKeyboard(rows);

        message.setReplyMarkup(inlineKeyboardMarkup);
    }
}
