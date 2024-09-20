package ru.tuganov.broker

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.tuganov.dto.InstrumentDBDto
import ru.tuganov.services.InstrumentService


@Component
class Receiver (
    private val instrumentService: InstrumentService
){

//    private val logger = LoggerFactory.getLogger(Receiver::class.java)
    @RabbitListener(queues = ["getInstrumentsQueue"])
    fun getInstrumentsByChatId(chatId: Long): MutableList<InstrumentDBDto> = instrumentService.getInstrumentsByChatId(chatId)

    @RabbitListener(queues = ["getInstrumentQueue"])
    fun getInstrument(instrumentId: Long): InstrumentDBDto = instrumentService.getInstrumentById(instrumentId)

    @RabbitListener(queues = ["deleteInstrumentQueue"])
    fun deleteInstrument(instrumentId: Long): Boolean = instrumentService.deleteInstrument(instrumentId)

    @RabbitListener(queues = ["saveInstrumentQueue"])
    fun saveInstrument(instrumentDBDto: InstrumentDBDto): Long = instrumentService.saveInstrument(instrumentDBDto)

    @RabbitListener(queues = ["getAllInstrumentsQueue"])
    fun getAllInstruments(text: String): MutableList<InstrumentDBDto> = instrumentService.getAllInstruments()
}