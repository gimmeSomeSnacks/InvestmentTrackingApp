package ru.tuganov.bot.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.commands.Command;
import ru.tuganov.bot.commands.MenuCommand;
import ru.tuganov.bot.commands.StartCommand;
import ru.tuganov.bot.utils.Message;

import java.util.Map;

@Service
public class CommandHandler {
    private final Map<String, Command> commands;

    public CommandHandler() {
        this.commands = Map.of(
                "/start", new StartCommand(),
                "/menu", new MenuCommand()
        );
    }

    public SendMessage handleCommands(Update update, Map<Long, String> userContext) {
        userContext.put(update.getMessage().getChatId(), "");
        var messageText = update.getMessage().getText();
        var command = commands.get(messageText.trim());
        if (command == null) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), Message.unknownCommand);
        } else {
            return command.apply(update);
        }
    }
}
