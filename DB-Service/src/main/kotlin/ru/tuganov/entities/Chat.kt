package ru.tuganov.entities

import jakarta.persistence.*


@Entity
class Chat (
    @Id
    val id: Long,
    @OneToMany(mappedBy = "chat", cascade = [(CascadeType.ALL)])
    val instruments: MutableList<Instrument>
){
}