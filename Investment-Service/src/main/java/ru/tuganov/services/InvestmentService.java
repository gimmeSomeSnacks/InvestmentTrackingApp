package ru.tuganov.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.GetOrderBookResponse;
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
        var instrument = instrumentsService.getInstrumentByFigi(figi).join();
        var status = instrument.getTradingStatus();
        var type = instrument.getInstrumentType();
        if (type.equals("share") && figi.startsWith("BBG") && (status == SecurityTradingStatus.SECURITY_TRADING_STATUS_NORMAL_TRADING ||
            status == SecurityTradingStatus.SECURITY_TRADING_STATUS_BREAK_IN_TRADING ||
            status == SecurityTradingStatus.SECURITY_TRADING_STATUS_DEALER_BREAK_IN_TRADING ||
            status == SecurityTradingStatus.SECURITY_TRADING_STATUS_DEALER_NORMAL_TRADING)) {
            GetOrderBookResponse orderBook = marketDataService.getOrderBook(figi, 1).join();
            if (orderBook != null && !orderBook.getAsksList().isEmpty()) {
                var price = orderBook.getAsks(0).getPrice();
                var sellPrice = price.getUnits() + price.getNano() / Math.pow(10.0, 9);
                price = orderBook.getBids(0).getPrice();
                var buyPrice = price.getUnits() + price.getNano() / Math.pow(10.0, 9);
                log.info("sell: {}, buy: {}", sellPrice, buyPrice);
                if (sellPrice != 0 && buyPrice != 0) {
                    return new InstrumentDto(instrumentShort.getName(),
                            instrumentShort.getFigi(),
                            sellPrice,
                            buyPrice);
                }
            }
        }
        return null;
    }

    private ArrayList<InstrumentDto> dtoListParser(List<InstrumentShort> instrumentShorts) {
        ArrayList<InstrumentDto> instrumentDtos = new ArrayList<>();
        for (InstrumentShort instrumentShort : instrumentShorts) {
            var instrumentDto = dtoParser(instrumentShort);
            if (instrumentDto != null) {
                instrumentDtos.add(instrumentDto);
            }
        }
        return instrumentDtos;
    }
}
