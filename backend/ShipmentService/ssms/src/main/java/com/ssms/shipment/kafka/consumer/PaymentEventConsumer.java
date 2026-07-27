package com.ssms.shipment.kafka.consumer;


import com.ssms.common.event.PaymentCompletedEvent;
import com.ssms.shipment.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {
    private final ShipmentService shipmentService;

    @KafkaListener(topics = "Payment-completed-topic",groupId = "shipment-group")
    public void consumePaymentEvent(PaymentCompletedEvent event){
        shipmentService.dispatchShipment(event.getShipmentId());
    }
}
