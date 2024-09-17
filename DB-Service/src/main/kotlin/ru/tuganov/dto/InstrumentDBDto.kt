package ru.tuganov.dto

class InstrumentDBDto(
    val instrumentId: Long = 0L,
    val chatId: Long = 0L,
    val figi: String = "",
    val sellPrice: Double = 0.0,
    val buyPrice: Double = 0.0
) {
}