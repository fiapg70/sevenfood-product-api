package br.com.sevenfood.product.sevenfoodproductapi.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentApprovedListener {

    @EventListener
    public void handlePaymentApprovedEvent(PaymentApprovedEvent event) {
        System.out.println(event);
    }
}
