package com.ssms.billing.kafka.producer;

import com.ssms.common.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public void sendInvoiceCreatedEvent(PaymentCompletedEvent event){
        kafkaTemplate.send("Payment-completed-topic",event);
    }
}
