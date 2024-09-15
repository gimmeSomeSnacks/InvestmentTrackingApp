package ru.tuganov.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tuganov.dto.InstrumentDBDto
import ru.tuganov.entities.Chat
import ru.tuganov.entities.Instrument
import ru.tuganov.repository.InstrumentRepository

@Service
class InstrumentService (
    private val instrumentRepository : InstrumentRepository,
    private val chatService: ChatService
){

    private val logger = LoggerFactory.getLogger(InstrumentService::class.java)
    fun getInstrumentsByChatId(chatId: Long): MutableList<InstrumentDBDto> {
        val chat = chatService.getChat(chatId)
        logger.info("im here dude")
        if (chat == null) {
            logger.info("No chat found with id $chatId")
            return mutableListOf();
        } else {
            return parseListDto(chat.instruments)
        }
    }

    fun parseListDto(instuments: List<Instrument>): MutableList<InstrumentDBDto> {
        val instrumentList: MutableList<InstrumentDBDto> = mutableListOf();
        for (instrument in instuments) {
            instrumentList.add(parseDto(instrument))
        }
        return instrumentList
    }

    fun parseDto(instrument: Instrument): InstrumentDBDto {
        return InstrumentDBDto(
            instrument.id,
            instrument.chat.id,
            instrument.figi,
            instrument.maxPrice,
            instrument.minPrice
        )
    }

    fun saveInstrument(instrumentDBDto: InstrumentDBDto) {
        logger.info(instrumentDBDto.figi)
        val chatId = instrumentDBDto.chatId
        var chat = chatService.getChat(chatId)
        if (chat == null) {
            chat = Chat(instrumentDBDto.chatId, mutableListOf())
            chatService.saveChat(chat)
        }

        val instrument = Instrument(instrumentDBDto.figi,
                                    instrumentDBDto.maxPrice,
                                    instrumentDBDto.minPrice,
                                    chat)
        logger.info(instrumentDBDto.instrumentId.toString())
        if (instrumentDBDto.instrumentId != 0L) {
            instrument.id = instrumentDBDto.instrumentId
        }
        instrumentRepository.save(instrument)
    }

    fun getInstrumentById(instrumentId: Long): InstrumentDBDto {
        val instrument = instrumentRepository.findInstrumentById(instrumentId)
        return parseDto(instrument)
    }

    @Transactional
    fun deleteInstrument(instrumentId: Long) {
        logger.info("id DB SERVICE: $instrumentId")
        val instrument = instrumentRepository.findInstrumentById(instrumentId)
        val chatId = instrument.chat.id
        val chat = chatService.getChat(chatId)
        chat?.instruments?.remove(instrument)
        if (chat != null) chatService.saveChat(chat)

        instrumentRepository.flush()
        instrumentRepository.deleteById(instrumentId)
        logger.info("all instruments in this chat: " + chatService.getChat(chatId)?.instruments?.size)
    }

    fun getAllInstruments(): MutableList<InstrumentDBDto> = parseListDto(instrumentRepository.findAll())
}