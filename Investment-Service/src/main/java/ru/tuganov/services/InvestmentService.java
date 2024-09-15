package ru.tuganov.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.contract.v1.SecurityTradingStatus;
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
        return dtoListParser(instrumentShorts);
    }

    private InstrumentDto dtoParser(InstrumentShort instrumentShort) {
        var figi = instrumentShort.getFigi();
        var isAvailable = instrumentsService.getInstrumentByFigi(figi).join().getTradingStatus();
//        log.info("status: {}", isAvailable);
        if (isAvailable == SecurityTradingStatus.SECURITY_TRADING_STATUS_NORMAL_TRADING ||
            isAvailable == SecurityTradingStatus.SECURITY_TRADING_STATUS_BREAK_IN_TRADING ||
            isAvailable == SecurityTradingStatus.SECURITY_TRADING_STATUS_DEALER_BREAK_IN_TRADING ||
            isAvailable == SecurityTradingStatus.SECURITY_TRADING_STATUS_DEALER_NORMAL_TRADING) {
            var priceee = instrumentsService.getInstrumentByFigi(figi).join();
            var price = marketDataService.getLastPrices(List.of(figi)).join().getFirst().getPrice();
            if (price.getUnits() != 0) {
                return new InstrumentDto(instrumentShort.getName(),
                        instrumentShort.getFigi(),
                        price.getUnits());
            }
        }
        return null;
        //price.getNano() выдаст еще допом дробную часть, но пока что рано
    }

    private ArrayList<InstrumentDto> dtoListParser(List<InstrumentShort> instrumentShorts) {
        ArrayList<InstrumentDto> instrumentDtos = new ArrayList<>();
        for (InstrumentShort instrumentShort : instrumentShorts) {
            var instrumentDto = dtoParser(instrumentShort);
            if (instrumentDto != null) {
                instrumentDtos.add(instrumentDto);
            }
        }
        log.info("size: {}", instrumentDtos.size());
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
