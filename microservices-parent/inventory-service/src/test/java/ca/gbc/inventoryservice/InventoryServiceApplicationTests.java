package ca.gbc.inventoryservice;

import ca.gbc.inventoryservice.controller.InventoryController;
import ca.gbc.inventoryservice.dto.InventoryRequest;
import ca.gbc.inventoryservice.dto.InventoryResponse;
import ca.gbc.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
public class InventoryServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryService inventoryService;

    @Test
    public void isInStock() throws Exception {
        InventoryRequest request = new InventoryRequest("sku_12345", 2);
        List<InventoryResponse> mockResponse = Arrays.asList(new InventoryResponse("sku_12345", true));

        when(inventoryService.isInStock(Arrays.asList(request))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(request))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].skuCode").value("sku_12345"))
                .andExpect(jsonPath("$[0].sufficientStock").value(true));
    }
}