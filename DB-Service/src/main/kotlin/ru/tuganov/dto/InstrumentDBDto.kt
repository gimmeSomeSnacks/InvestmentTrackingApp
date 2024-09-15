package ru.tuganov.dto

class InstrumentDBDto(
    val instrumentId: Long = 0L,
    val chatId: Long = 0,
    val figi: String = "",
    val maxPrice: Double = 0.0,
    val minPrice: Double = 0.0
) {
}