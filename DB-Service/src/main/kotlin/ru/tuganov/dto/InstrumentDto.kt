package ru.tuganov.dto

class InstrumentDto(
    val chatId: Long,
    val figi: String,
    val minPrice: Double,
    val maxPrice: Double
) {
}