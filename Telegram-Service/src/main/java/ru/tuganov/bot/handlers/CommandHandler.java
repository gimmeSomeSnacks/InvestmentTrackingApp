package ru.tuganov.bot.handlers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tuganov.bot.commands.Command;
import ru.tuganov.bot.commands.MenuCommand;
import ru.tuganov.bot.commands.StartCommand;

import java.util.Map;

@Slf4j
@Component
public class CommandHandler {
    private final Map<String, Command> commands;

    public CommandHandler() {
        this.commands = Map.of(
                "/start", new StartCommand(),
                "/menu", new MenuCommand()
        );
    }

    public SendMessage handleCommands(Update update) {
        var messageText = update.getMessage().getText();
        var command = commands.get(messageText.trim());
        return command.apply(update);
    }
}
