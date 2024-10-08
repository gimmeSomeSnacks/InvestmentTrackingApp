package ru.tuganov.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentDBDto {
    private Long instrumentId;
    private Long chatId;
    private String figi;
    private Double sellPrice;
    private Double buyPrice;
}
