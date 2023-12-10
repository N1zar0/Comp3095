package ca.gbc.orderservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// not actually important for the lession, apparently
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {

    private String  orderNumber;
}
