package ru.tuganov.dto

class InstrumentDBDto(
    val instrumentId: Long = -1,
    val chatId: Long = -1,
    val figi: String = "",
    val sellPrice: Double = -1.0,
    val buyPrice: Double = -1.0
) {
}