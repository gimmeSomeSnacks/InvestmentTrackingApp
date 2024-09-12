package ru.tuganov.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.MarketDataService;
import ru.tuganov.dto.InstrumentDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestmentService {
    private final InstrumentsService instrumentsService;
    private final MarketDataService marketDataService;

    public InstrumentDto getInstrument(String instrumentQuery) {
        var instrument = instrumentsService.findInstrument(instrumentQuery).join().get(0);
        return dtoParser(instrument);
    }

    public ArrayList<InstrumentDto> getInstruments(String instrumentQuery) {
        var instrumentShorts = instrumentsService.findInstrument(instrumentQuery).join();
        log.info("Кол-во вещей: {}", instrumentShorts.size());
        return dtoListParser(instrumentShorts);
    }

    private InstrumentDto dtoParser(InstrumentShort instrumentShort) {
        var price = marketDataService.getLastPrices(List.of(instrumentShort.getFigi())).join().get(0).getPrice();
        return new InstrumentDto(instrumentShort.getName(),
                instrumentShort.getFigi(),
                price.getUnits());
        //price.getNano() выдаст еще допом дробную часть, но пока что рано
    }

    private ArrayList<InstrumentDto> dtoListParser(List<InstrumentShort> instrumentShorts) {
        ArrayList<InstrumentDto> instrumentDtos = new ArrayList<>();
        for (InstrumentShort instrumentShort : instrumentShorts) {
            instrumentDtos.add(dtoParser(instrumentShort));
        }
        return instrumentDtos;
    }


//    public double getLastPrice(String figi) {
//        var instrument = marketDataService.getLastPrices(List.of(figi)).join();
//        var price = instrument.get(0).getPrice();
//        return price.getUnits() + price.getNano();
//    }

//    public String getInstrumentName(String instrumentQuery) {
//        return instrumentsService.findInstrument(instrumentQuery).join().get(0).getName();
//    }
}
