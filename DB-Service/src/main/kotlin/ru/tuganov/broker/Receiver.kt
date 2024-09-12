package ru.tuganov.broker

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.tuganov.dto.InstrumentDto
import ru.tuganov.entities.Instrument
import ru.tuganov.services.InstrumentService


@Component
class Receiver (
    private val instrumentService: InstrumentService
){
    @RabbitListener(queues = ["getInstrumentsQueue"])
    fun getInstrumentsByChatId(chatId: Long): MutableList<InstrumentDto> = instrumentService.getInstrumentsByChatId(chatId)

    @RabbitListener(queues = ["getInstrumentQueue"])
    fun getInstrument(figi: String): Instrument = instrumentService.getInstrumentByFigi(figi)

    @RabbitListener(queues = ["deleteInstrumentQueue"])
    fun deleteInstrument(figi: String) = instrumentService.deleteInstrument(figi)

    @RabbitListener(queues = ["saveInstrumentQueue"])
    fun saveInstrument(instrumentDto: InstrumentDto) = instrumentService.saveInstrument(instrumentDto)
}