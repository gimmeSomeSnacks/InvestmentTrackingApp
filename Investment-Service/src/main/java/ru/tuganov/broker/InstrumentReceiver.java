package ru.tuganov.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tuganov.dto.InstrumentDto;
import ru.tuganov.services.InvestmentService;

import java.util.ArrayList;

@RequiredArgsConstructor
@Slf4j
@Component
public class InstrumentReceiver {
    private final InvestmentService investmentService;

    @RabbitListener(queues = "instrumentQueue")
    public InstrumentDto getInstrument(String instrumentQuery) {
        return investmentService.getInstrument(instrumentQuery);
    }

    @RabbitListener(queues = "instrumentListQueue")
    public ArrayList<InstrumentDto> getInstrumentList(String instrumentQuery) {
        log.info("getInstrumentList: {}", instrumentQuery);
        return investmentService.getInstruments(instrumentQuery);
    }
}