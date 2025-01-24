package com.github.kegszool.bot.handler.response.impl;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Log4j2
public class ServiceExceptionResponseHandler extends BaseResponseHandler {

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    @Autowired
    public ServiceExceptionResponseHandler(MessageUtils messageUtils) {
        super(messageUtils);
    }

    @Override
    public boolean canHandle(String routingKey) {
        return SERVICE_EXCEPTION_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public PartialBotApiMethod<?> handle(ServiceMessage serviceMessage) {
        var answerMessage = new SendMessage(serviceMessage.getChatId(), "Service Exception");
        return answerMessage;
    }
}