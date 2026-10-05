package com.warehouse.lagerkern.catalog.api;
import com.warehouse.lagerkern.catalog.application.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {
    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public ResponseEntity<BatchResponseDTO> createBatch(@Valid @RequestBody BatchRequestDTO batchRequestDTO) {
        BatchResponseDTO batchResponseDTO = batchService.createBatch(batchRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(batchResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponseDTO> getBatchById(@PathVariable Long id) {
        BatchResponseDTO response = batchService.getBatchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BatchResponseDTO>> getAllBatches() {
        List<BatchResponseDTO> response = batchService.getAllBatches();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

