package ru.tuganov.services

import org.springframework.stereotype.Service
import ru.tuganov.entities.Chat
import ru.tuganov.repository.ChatRepository

@Service
class ChatService (
    private val chatRepository: ChatRepository
){
    fun getChat(chatId: Long): Chat? = chatRepository.findChatById(chatId)

    fun saveChat(chat: Chat) = chatRepository.save(chat)
}