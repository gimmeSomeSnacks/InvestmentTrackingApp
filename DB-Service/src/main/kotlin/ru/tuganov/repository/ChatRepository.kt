package ru.tuganov.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.tuganov.entities.Chat

@Repository
interface ChatRepository: JpaRepository<Chat, Long> {
    fun findChatById(id: Long): Chat?
}