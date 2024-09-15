package ru.tuganov.broker.senders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.tuganov.dto.InstrumentDBDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSender {
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange saveInstrumentExchange;
    private final DirectExchange getInstrumentExchange;
    private final DirectExchange deleteInstrumentExchange;
    private final DirectExchange getInstrumentsExchange;

    public List<InstrumentDBDto> getInstruments(Long chatId) {
        var instruments = rabbitTemplate.convertSendAndReceive(getInstrumentsExchange.getName(), "getInstruments", chatId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        if (instruments != null) {
            log.info("Instruments received");
            return (List<InstrumentDBDto>) instruments;
        }
        log.info("No instruments received");
        return null;
    }

    public void saveInstrument(InstrumentDBDto instrument) {
        log.info("second step:{}", instrument.getFigi());
        rabbitTemplate.convertAndSend(saveInstrumentExchange.getName(), "saveInstrument", instrument);
    }

    public void deleteInstrument(Long instrumentId) {
        rabbitTemplate.convertAndSend(deleteInstrumentExchange.getName(), "deleteInstrument", instrumentId);
    }

    public InstrumentDBDto getInstrument(Long instrumentId) {
        var instrument = rabbitTemplate.convertSendAndReceive(getInstrumentExchange.getName(), "getInstrument", instrumentId);
        if (instrument != null) {
            return (InstrumentDBDto) instrument;
        }
        return null;
    }
}
