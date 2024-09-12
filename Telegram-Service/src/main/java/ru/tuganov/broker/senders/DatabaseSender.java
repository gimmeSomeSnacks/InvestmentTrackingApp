package ru.tuganov.broker.senders;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.tuganov.dto.InstrumentDBDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSender {
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange saveInstrumentExchange;
    private final DirectExchange getInstrumentExchange;
    private final DirectExchange deleteInstrumentExchange;
    private final DirectExchange getInstrumentsExchange;

    public List<InstrumentDBDto> getInstruments(Long chatId) {
        return (List<InstrumentDBDto>) rabbitTemplate.convertSendAndReceive(getInstrumentsExchange.getName(), "getInstruments", chatId);
    }

    public void saveInstrument(InstrumentDBDto instrument) {
        rabbitTemplate.convertAndSend(saveInstrumentExchange.getName(), "saveInstrument", instrument);
    }

    public void deleteInstrument(String figi) {
        rabbitTemplate.convertAndSend(deleteInstrumentExchange.getName(), "deleteInstrument", figi);
    }

    public InstrumentDBDto getInstrument(String figi) {
        return (InstrumentDBDto) rabbitTemplate.convertSendAndReceive(getInstrumentExchange.getName(), "getInstrument", figi);
    }
}
