package com.warehouse.lagerkern.catalog.application;

import com.warehouse.lagerkern.catalog.api.BatchRequestDTO;
import com.warehouse.lagerkern.catalog.api.BatchResponseDTO;
import com.warehouse.lagerkern.catalog.domain.Batch;
import com.warehouse.lagerkern.catalog.domain.Item;
import com.warehouse.lagerkern.catalog.domain.exception.BatchNotFoundException;
import com.warehouse.lagerkern.catalog.infrastructure.BatchRepository;
import com.warehouse.lagerkern.catalog.infrastructure.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {
    private final BatchRepository batchRepository;
    private final ItemRepository itemRepository;

    public BatchService(BatchRepository batchRepository, ItemRepository itemRepository) {
        this.batchRepository = batchRepository;
        this.itemRepository = itemRepository;
    }

    // 2. READ Operation (Get by ID)
    public BatchResponseDTO getBatchById(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new BatchNotFoundException(id));

        return mapToDto(batch);
    }

    // 3. READ Operation (Get All Batches)
    public List<BatchResponseDTO> getAllBatches() {
        return batchRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    // Helper mapping method
    private BatchResponseDTO mapToDto(Batch batch) {
        return new BatchResponseDTO(
                batch.getId(),
                batch.getItem().getId(),
                batch.getBatchNo(),
                batch.getBestBefore()
        );
    }

    @Transactional
    public BatchResponseDTO createBatch(BatchRequestDTO request) {
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Batch batch = new Batch(
                item,
                request.batchNo(),
                request.bestBefore()
        );

        Batch savedBatch = batchRepository.save(batch);

        return new BatchResponseDTO(
                savedBatch.getId(),
                savedBatch.getItem().getId(),
                savedBatch.getBatchNo(),
                savedBatch.getBestBefore()
        );
    }

    @Transactional
    public void deleteBatch(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));

        batchRepository.delete(batch);
    }
}
