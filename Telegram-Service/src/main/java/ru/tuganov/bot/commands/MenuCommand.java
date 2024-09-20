package ru.tuganov.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.utils.InstrumentsMarkup;
import ru.tuganov.bot.utils.Message;
import ru.tuganov.dto.MarkupDataDto;

import java.util.List;

@Slf4j
@Component
public class MenuCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        var sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Меню действий");
        log.info(sendMessage.getText());
        List<MarkupDataDto> markupDataDtoList = List.of(
                new MarkupDataDto(Message.instrumentList, "simpleGUI"),
                new MarkupDataDto(Message.addInstrument, "contextAI"));
        InstrumentsMarkup.setMenu(sendMessage, markupDataDtoList);
        return sendMessage;
    }
}
