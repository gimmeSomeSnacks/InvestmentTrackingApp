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
    private final DirectExchange getAllInstrumentsExchange;

    public List<InstrumentDBDto> getInstruments(Long chatId) {
        var instruments = rabbitTemplate.convertSendAndReceive(getInstrumentsExchange.getName(), "getInstruments", chatId);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            log.error(e.getMessage());
//        }
        if (instruments != null) {
            return (List<InstrumentDBDto>) instruments;
        }
        return null;
    }

    public Long saveInstrument(InstrumentDBDto instrument) {
        return (Long) rabbitTemplate.convertSendAndReceive(saveInstrumentExchange.getName(), "saveInstrument", instrument);
    }

    public Boolean deleteInstrument(Long instrumentId) {
        return (Boolean) rabbitTemplate.convertSendAndReceive(deleteInstrumentExchange.getName(), "deleteInstrument", instrumentId);
    }

    public InstrumentDBDto getInstrument(Long instrumentId) {
        var instrument = rabbitTemplate.convertSendAndReceive(getInstrumentExchange.getName(), "getInstrument", instrumentId);
        return (InstrumentDBDto) instrument;
    }

    public List<InstrumentDBDto> getAllInstruments() {
        var instruments = (List<InstrumentDBDto>) rabbitTemplate.convertSendAndReceive(getAllInstrumentsExchange.getName(), "getAllInstruments", "text");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            log.error(e.getMessage());
//        }
        return instruments;
    }
}
