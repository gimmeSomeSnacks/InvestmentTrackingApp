package ru.tuganov.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.tuganov.dto.InstrumentDto;
import ru.tuganov.dto.PricesDto;
import ru.tuganov.services.InvestmentService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class InstrumentReceiver {
    private final InvestmentService investmentService;

    @RabbitListener(queues = "instrumentQueue")
    public InstrumentDto getInstrument(String instrumentQuery) {
        return investmentService.getInstrument(instrumentQuery);
    }

    @RabbitListener(queues = "instrumentListQueue")
    public ArrayList<InstrumentDto> getInstrumentList(String instrumentQuery) {
        return investmentService.getInstruments(instrumentQuery);
    }

    @RabbitListener(queues = "instrumentPricesQueue")
    public ArrayList<PricesDto> getInstrumentPrices(String instrumentQuery) {
        return investmentService.getInstrumentPrices(instrumentQuery);
    }
}