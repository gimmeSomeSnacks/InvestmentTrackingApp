package ru.tuganov.investment;

public record AchievedInstrument(Long id, Long chatId, String figi, Double currentPrice, Double fixedPrice) {
}
