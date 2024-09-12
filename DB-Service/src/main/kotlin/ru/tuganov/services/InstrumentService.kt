package ru.tuganov.services

import org.springframework.stereotype.Service
import ru.tuganov.dto.InstrumentDto
import ru.tuganov.entities.Chat
import ru.tuganov.entities.Instrument
import ru.tuganov.repository.InstrumentRepository

@Service
class InstrumentService (
    private val instrumentRepository : InstrumentRepository,
    private val chatService: ChatService
){
    fun getInstrumentsByChatId(chatId: Long): MutableList<InstrumentDto> {
        val chat = chatService.getChat(chatId)
        if (chat == null) {
            return mutableListOf();
        }
        return parseDto(chat.instruments, chatId)
    }

    fun parseDto(instuments: List<Instrument>, chatId: Long): MutableList<InstrumentDto> {
        val instrumentList: MutableList<InstrumentDto> = mutableListOf();
        for (instrument in instuments) {
            val instrumentDto = InstrumentDto(chatId,
                instrument.figi,
                instrument.minPrice,
                instrument.maxPrice
            )
            instrumentList.add(instrumentDto)
        }
        return instrumentList
    }

    fun saveInstrument(instrumentDto: InstrumentDto) {
        val chatId = instrumentDto.chatId
        var chat = chatService.getChat(chatId)
        if (chat == null) {
            chat = Chat(instrumentDto.chatId, mutableListOf())
            chatService.saveChat(chat)
        }
        val instrument = Instrument(instrumentDto.figi,
                                    instrumentDto.maxPrice,
                                    instrumentDto.minPrice,
                                    chat)
        instrumentRepository.save(instrument)
    }

    fun getInstrumentByFigi(figi: String): Instrument = instrumentRepository.findInstrumentByFigi(figi)

    fun deleteInstrument(figi: String) = instrumentRepository.deleteInstrumentByFigi(figi)
}