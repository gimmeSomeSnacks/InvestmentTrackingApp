package ru.tuganov.investment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tuganov.bot.TelegramBot;
import ru.tuganov.broker.senders.DatabaseSender;
import ru.tuganov.broker.senders.InvestmentSender;
import ru.tuganov.dto.InstrumentDBDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstrumentObserver {
    private final DatabaseSender databaseSender;
    private final InvestmentSender investmentSender;

//    @Scheduled(fixedRateString = "${scheduling.fixed-rate}")
    private void checkInstrumentsPrices() {
        var instruments = databaseSender.getAllInstruments();
        if (instruments == null) {
            return;
        }
        var chunkSize = instruments.size() / 5 + (instruments.size() % 5 == 0 ? 0 : 1);
        AtomicInteger index = new AtomicInteger(0);
        List<List<InstrumentDBDto>> instrumentsChunks = instruments.stream()
                .collect(Collectors.groupingBy(x -> index.getAndIncrement() / chunkSize))
                .values().stream().toList();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (List<InstrumentDBDto> chunk : instrumentsChunks) {
            executorService.submit(() -> {
                for (InstrumentDBDto instrumentDBDto : chunk) {
                    checkPrice(instrumentDBDto);
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void checkPrice(InstrumentDBDto instrumentDBDto) {

        var figi = instrumentDBDto.getFigi();
        var instrument = investmentSender.getInstrument(figi);

        var fixedSellPrice = instrumentDBDto.getSellPrice();
        var fixedBuyPrice = instrumentDBDto.getBuyPrice();

        var currentSellPrice = instrument.sellPrice();
        var currentBuyPrice = instrument.buyPrice();

        var fixedPrice = 0D;
        var currentPrice = 0D;
        var achieved = false;
        if (fixedSellPrice <= currentSellPrice) {
            achieved = true;
            fixedPrice = fixedSellPrice;
            currentPrice = currentSellPrice;

        } else if (fixedBuyPrice >= currentBuyPrice) {
            achieved = true;
            fixedPrice = fixedBuyPrice;
            currentPrice = currentBuyPrice;
        }

        if (achieved) {
            var achievedInstrument = new AchievedInstrument(
                    instrumentDBDto.getInstrumentId(),
                    instrumentDBDto.getChatId(),
                    figi,
                    currentPrice,
                    fixedPrice
            );
            log.info("achieved instrument: {} {}", achievedInstrument.currentPrice(), achievedInstrument.fixedPrice());
            TelegramBot.achievedInstruments.add(achievedInstrument);
        }
    }
}
