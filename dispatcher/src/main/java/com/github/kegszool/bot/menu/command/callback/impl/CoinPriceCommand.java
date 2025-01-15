package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

//TODO: рефакторинг класса + логирование

@Component
@Log4j2
public class CoinPriceCommand extends CallbackCommand {

    private final MessageUtils messageUtils;
    private final RequestProducerService requestService;

    @Value("${coin.prefix}")
    private String COIN_PREFIX;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request_key}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    @Autowired
    public CoinPriceCommand(
            MessageUtils messageUtils,
            RequestProducerService requestService
    ) {
        this.messageUtils = messageUtils;
        this.requestService = requestService;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(COIN_PREFIX);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        String cryptocurrencyName = getCryptocurrencyNameByCallbackData(query);
        log.info("The process of getting the price of a coin: '{}'", cryptocurrencyName);

        Long chatId = query.getMessage().getChatId();
        var dataTransferObject = new ServiceMessage();
        dataTransferObject.setData(cryptocurrencyName);
        dataTransferObject.setChatId(chatId);
        requestService.produce(COIN_PRICE_REQUEST_ROUTING_KEY, dataTransferObject);

        String text = String.format("Вы выбрали монету: %s", cryptocurrencyName);
        return messageUtils.createEditMessage(query, text);
    }

    private String getCryptocurrencyNameByCallbackData(CallbackQuery query) {
        String data = query.getData();
        int lengthOfCoinPrefix = COIN_PREFIX.length();
        int lengthOfData = data.length();
        return data.substring(lengthOfCoinPrefix, lengthOfData);
    }
}