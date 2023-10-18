package ca.gbc.orderservice;
import ca.gbc.orderservice.dto.InventoryResponse;
import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.model.OrderLineItem;
import ca.gbc.orderservice.repository.OrderRepository;
import ca.gbc.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
public class OrderServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testPlaceOrderSuccess() {
        // Mocking inventory service response
        when(webTestClient.post().uri(any(String.class))
                .exchange()
                .expectStatus().isOk()
                .returnResult(InventoryResponse.class)
                .getResponseBody())
                .thenReturn(Mono.just(Collections.singletonList(new InventoryResponse(true))));

        OrderServiceImpl orderService = new OrderServiceImpl(orderRepository, webTestClient.mutate());

        OrderLineItemDto lineItemDto = new OrderLineItemDto(43, "sdad", 20.30,3);
        OrderRequest orderRequest = new OrderRequest(Collections.singletonList(lineItemDto));

        Order expectedOrder = new Order();
        expectedOrder.setOrderNumber("1234567890");
        expectedOrder.setOrderLineItemList(Collections.singletonList(new OrderLineItem("SKU123", 10, 20.0)));

        orderService.placeOrder(orderRequest);

        verify(orderRepository, times(1)).save(expectedOrder);
    }

    @Test
    public void testPlaceOrderFailureInsufficientStock() {
        // Mocking inventory service response with insufficient stock
        when(webTestClient.post().uri(any(String.class))
                .exchange()
                .expectStatus().isOk()
                .returnResult(InventoryResponse.class)
                .getResponseBody())
                .thenReturn(Mono.just(Collections.singletonList(new InventoryResponse(false))));

        OrderServiceImpl orderService = new OrderServiceImpl(orderRepository, webTestClient.mutate());

        OrderLineItemDto lineItemDto = new OrderLineItemDto("SKU123", 10, 20.0);
        OrderRequest orderRequest = new OrderRequest(Collections.singletonList(lineItemDto));

        try {
            orderService.placeOrder(orderRequest);
        } catch (RuntimeException e) {
            // Ensure that RuntimeException "Insufficient stock" is thrown
            assert e.getMessage().equals("Insufficient stock");
        }

        verify(orderRepository, never()).save(any(Order.class));
    }
}