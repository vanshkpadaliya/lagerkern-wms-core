package com.warehouse.lagerkern.catalog.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.warehouse.lagerkern.catalog.api.BatchRequestDTO;
import com.warehouse.lagerkern.catalog.api.BatchResponseDTO;
import com.warehouse.lagerkern.catalog.domain.Batch;
import com.warehouse.lagerkern.catalog.domain.Item;
import com.warehouse.lagerkern.catalog.domain.exception.BatchNotFoundException;
import com.warehouse.lagerkern.catalog.infrastructure.BatchRepository;
import com.warehouse.lagerkern.catalog.infrastructure.ItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchServiceTest {

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BatchService batchService;

    private Item sampleItem;
    private Batch sampleBatch;
    private BatchRequestDTO sampleRequest;

    @BeforeEach
    void setUp() {
        // Mock sample item (ID: 1L)
        sampleItem = new Item(
                "SKU-BATCH",
                "Test Item",
                "PCS",
                null,
                true
        );
        // Assuming your Item entity has a way to ensure ID is populated or mocked,
        // but for testing, let's look at how the DTO maps item ID.

        sampleBatch = new Batch(
                sampleItem,
                "BATCH-001",
                LocalDate.of(2027, 12, 31)
        );

        sampleRequest = new BatchRequestDTO(
                1L,
                "BATCH-001",
                LocalDate.of(2027, 12, 31)
        );
    }

    // ==========================================
    // 1. TESTS FOR getBatchById
    // ==========================================

    @Test
    void testGetBatchById_Success() {
        when(batchRepository.findById(1L)).thenReturn(Optional.of(sampleBatch));

        BatchResponseDTO response = batchService.getBatchById(1L);

        assertNotNull(response);
        assertEquals("BATCH-001", response.batchNo());
        verify(batchRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBatchById_NotFound_ThrowsException() {
        when(batchRepository.findById(99L)).thenReturn(Optional.empty());

        BatchNotFoundException exception = assertThrows(
                BatchNotFoundException.class,
                () -> batchService.getBatchById(99L)
        );


        verify(batchRepository, times(1)).findById(99L);
    }

    // ==========================================
    // 2. TESTS FOR getAllBatches
    // ==========================================

    @Test
    void testGetAllBatches_Success() {
        when(batchRepository.findAll()).thenReturn(List.of(sampleBatch));

        List<BatchResponseDTO> responses = batchService.getAllBatches();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("BATCH-001", responses.get(0).batchNo());
        verify(batchRepository, times(1)).findAll();
    }

    @Test
    void testGetAllBatches_EmptyList() {
        when(batchRepository.findAll()).thenReturn(List.of());

        List<BatchResponseDTO> responses = batchService.getAllBatches();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(batchRepository, times(1)).findAll();
    }

    // ==========================================
    // 3. TESTS FOR createBatch
    // ==========================================

    @Test
    void testCreateBatch_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        when(batchRepository.save(any(Batch.class))).thenReturn(sampleBatch);

        BatchResponseDTO response = batchService.createBatch(sampleRequest);

        assertNotNull(response);
        assertEquals("BATCH-001", response.batchNo());
        verify(itemRepository, times(1)).findById(1L);
        verify(batchRepository, times(1)).save(any(Batch.class));
    }

    @Test
    void testCreateBatch_ItemNotFound_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            batchService.createBatch(sampleRequest);
        });

        assertEquals("Item not found", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
        verify(batchRepository, never()).save(any(Batch.class));
    }

    @Test
    void testDeleteBatch_Success() {
        // Arrange: Mock the findById to return our sample batch
        when(batchRepository.findById(1L)).thenReturn(Optional.of(sampleBatch));

        // Act: Call the delete method
        batchService.deleteBatch(1L);

        // Assert: Verify it looked up the batch and then deleted it
        verify(batchRepository, times(1)).findById(1L);
        verify(batchRepository, times(1)).delete(sampleBatch);
    }

    @Test
    void testDeleteBatch_NotFound_ThrowsException() {
        // Arrange: Mock the findById to return empty (not found)
        when(batchRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert: Verify it throws the expected exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            batchService.deleteBatch(99L);
        });

        assertEquals("Batch not found with id: 99", exception.getMessage());

        // Verify it tried to find it, but NEVER called delete
        verify(batchRepository, times(1)).findById(99L);
        verify(batchRepository, never()).delete(any(Batch.class));
    }
}