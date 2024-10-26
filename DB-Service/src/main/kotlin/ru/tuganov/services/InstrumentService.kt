package ru.tuganov.services

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
    fun getInstrumentsByChatId(chatId: Long): MutableList<InstrumentDBDto> {
        val chat = chatService.getChat(chatId)
        if (chat == null) {
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
            instrument.sellPrice,
            instrument.buyPrice
        )
    }

    fun saveInstrument(instrumentDBDto: InstrumentDBDto): Long {
        val chatId = instrumentDBDto.chatId
        var chat = chatService.getChat(chatId)
        if (chat == null) {
            chat = Chat(instrumentDBDto.chatId, mutableListOf())
            chatService.saveChat(chat)
        }

        val instrument = Instrument(instrumentDBDto.figi,
                                    instrumentDBDto.sellPrice,
                                    instrumentDBDto.buyPrice,
                                    chat)
        if (instrumentDBDto.instrumentId != 0L) {
            instrument.id = instrumentDBDto.instrumentId
        }
        instrumentRepository.save(instrument)
        return instrument.id
    }

    fun getInstrumentById(instrumentId: Long): InstrumentDBDto {
        val instrument = instrumentRepository.findInstrumentById(instrumentId) ?: return InstrumentDBDto()
        return parseDto(instrument)
    }

    @Transactional
    fun deleteInstrument(instrumentId: Long): Boolean {
        val instrument = instrumentRepository.findInstrumentById(instrumentId) ?: return false
        val chatId = instrument.chat.id
        val chat = chatService.getChat(chatId)
        chat?.instruments?.remove(instrument)
        if (chat != null) chatService.saveChat(chat)

        instrumentRepository.flush()
        instrumentRepository.deleteById(instrumentId)
        return true;
    }

    fun getAllInstruments(): MutableList<InstrumentDBDto> = parseListDto(instrumentRepository.findAll())
}