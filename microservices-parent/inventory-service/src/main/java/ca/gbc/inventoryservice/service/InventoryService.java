package ca.gbc.inventoryservice.service;

import ca.gbc.inventoryservice.dto.InventoryRequest;
import ca.gbc.inventoryservice.dto.InventoryResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class InventoryService {

    public List<InventoryResponse> isInStock(List<InventoryRequest> requests) {
        log.info("Start of waiting period");

        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("End of waiting period");
        return null;
    }
}