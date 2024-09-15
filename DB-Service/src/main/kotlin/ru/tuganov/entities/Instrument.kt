package ru.tuganov.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "instruments")
class Instrument (
    val figi: String,
    val maxPrice: Double,
    val minPrice: Double,
    @ManyToOne
    @JoinColumn(name = "chat_id")
    val chat: Chat,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long = 0
){
}