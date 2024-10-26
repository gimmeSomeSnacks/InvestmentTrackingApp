package ru.tuganov.broker.senders;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.tuganov.dto.InstrumentDto;
import ru.tuganov.dto.PricesDto;

import java.util.ArrayList;


@Component
@RequiredArgsConstructor
@Slf4j
public class InvestmentSender {
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange instrumentListExchange;
    private final DirectExchange instrumentExchange;
    private final DirectExchange instrumentPricesExchange;

    public InstrumentDto getInstrument(String instrumentQuery) {
        return (InstrumentDto) rabbitTemplate.convertSendAndReceive(instrumentExchange.getName(),"instrument", instrumentQuery);
    }

    public ArrayList<InstrumentDto> getInstruments(String instrumentQuery) {
        var instruments = rabbitTemplate.convertSendAndReceive(instrumentListExchange.getName(), "instrumentList", instrumentQuery);
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
        if (instruments == null) {
            return null;
        }
        return (ArrayList<InstrumentDto>) instruments;
    }

    public ArrayList<PricesDto> getPrices(String instrumentQuery) {
        var instrumentPrices = rabbitTemplate.convertSendAndReceive(instrumentPricesExchange.getName(), "instrumentPrices", instrumentQuery);
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
        return (ArrayList<PricesDto>) instrumentPrices;
    }
}
