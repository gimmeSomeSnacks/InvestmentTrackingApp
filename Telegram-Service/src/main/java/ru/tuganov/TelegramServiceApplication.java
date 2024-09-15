package ru.tuganov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tuganov.bot.TelegramBotInitialization;

@SpringBootApplication
@EnableScheduling
public class TelegramServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramServiceApplication.class, args);
	}
}
