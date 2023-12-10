package ca.gbc.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics= "notificationTopic", groupId = "notificationGroup")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        log.info("sending out email notification for order number: {}")
                orderPlacedEvent.getOrderNumber();
    }
}
