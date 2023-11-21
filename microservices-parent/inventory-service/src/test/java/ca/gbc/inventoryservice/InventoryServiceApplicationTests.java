package ca.gbc.inventoryservice;
import ca.gbc.inventoryservice.controller.InventoryController;
import ca.gbc.inventoryservice.dto.InventoryRequest;
import ca.gbc.inventoryservice.dto.InventoryResponse;
import ca.gbc.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Test
    void testIsInStockEndpoint() throws Exception {
        List<InventoryRequest> requests = Arrays.asList(
                new InventoryRequest("SKU001", 2),
                new InventoryRequest("SKU002", 3)
        );

        List<InventoryResponse> responses = Arrays.asList(
                new InventoryResponse("SKU001", true),
                new InventoryResponse("SKU002", false)
        );

        when(inventoryService.isInStock(requests)).thenReturn(responses);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requests\": [{\"skuCode\": \"SKU001\", \"quantity\": 2}, {\"skuCode\": \"SKU002\", \"quantity\": 3}]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].skuCode").value("SKU001"))
                .andExpect(jsonPath("$[0].sufficientStock").value(true))
                .andExpect(jsonPath("$[1].skuCode").value("SKU002"))
                .andExpect(jsonPath("$[1].sufficientStock").value(false))
                .andReturn();
    }

}
