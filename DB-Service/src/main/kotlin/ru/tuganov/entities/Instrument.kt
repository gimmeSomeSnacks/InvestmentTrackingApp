package ru.tuganov.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Instrument (
    val figi: String,
    val maxPrice: Double,
    val minPrice: Double,
    @ManyToOne
    val chat: Chat,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
){
}