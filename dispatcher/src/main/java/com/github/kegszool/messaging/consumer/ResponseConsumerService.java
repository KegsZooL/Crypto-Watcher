package com.github.kegszool.messaging.consumer;

import com.github.kegszool.messaging.dto.ServiceMessage;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface ResponseConsumerService {
    void consume(ServiceMessage serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}