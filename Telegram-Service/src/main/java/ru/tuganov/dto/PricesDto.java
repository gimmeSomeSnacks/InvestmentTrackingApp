package ru.tuganov.dto;

import java.time.Instant;

public record PricesDto(Double close, Instant closeTime)  {
}
