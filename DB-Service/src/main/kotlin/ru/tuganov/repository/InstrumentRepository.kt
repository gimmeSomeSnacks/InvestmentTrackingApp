package ru.tuganov.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.tuganov.entities.Instrument

@Repository
interface InstrumentRepository: JpaRepository<Instrument, Long> {
    fun findInstrumentById(id: Long): Instrument
}