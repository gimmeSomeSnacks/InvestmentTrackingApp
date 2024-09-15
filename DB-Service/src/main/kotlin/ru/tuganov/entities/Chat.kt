package ru.tuganov.entities

import jakarta.persistence.*


@Entity
@Table(name = "chats")
class Chat (
    @Id
    val id: Long,
    @OneToMany(mappedBy = "chat", cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, orphanRemoval = true)
    val instruments: MutableList<Instrument>
){
}