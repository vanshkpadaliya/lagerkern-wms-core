package com.warehouse.lagerkern.catalog.application;

// Explicit JUnit assertions (no wildcards)
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Explicit Mockito methods (avoids JDK internal 'verify' and Hamcrest 'any')
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.lagerkern.catalog.api.ItemResponse;
import com.warehouse.lagerkern.catalog.api.ItemCreateRequest;
import com.warehouse.lagerkern.catalog.domain.Item;
import com.warehouse.lagerkern.catalog.infrastructure.ItemRepository;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item sampleItem;
    private ItemCreateRequest sampleRequest;

    @BeforeEach
    void setUp() {
        // Standard sample request
        sampleRequest = new ItemCreateRequest(
                "SKU-001",
                "Laptop",
                "PCS",
                new BigDecimal("5.0"),
                true
        );

        // Standard sample entity (mimicking a database record with ID 1L)
        sampleItem = new Item(
                "SKU-001",
                "Laptop",
                "PCS",
                new BigDecimal("5.0"),
                true
        );
        // Assuming there's a setter or constructor for ID, or we mock field behaviors.
        // If getId() is called, it returns null unless configured, so let's make sure our assertions handle it or mock the item.
    }

    // ==========================================
    // 1. TESTS FOR getItemById
    // ==========================================

    @Test
    void testGetItemById_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));

        ItemResponse response = itemService.getItemById(1L);

        assertNotNull(response);
        assertEquals("SKU-001", response.sku());
        assertEquals("Laptop", response.name());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void testGetItemById_NotFound_ThrowsException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            itemService.getItemById(99L);
        });

        assertEquals("Item not found", exception.getMessage());
        verify(itemRepository, times(1)).findById(99L);
    }

    // ==========================================
    // 2. TESTS FOR getAllItems
    // ==========================================

    @Test
    void testGetAllItems_Success() {
        when(itemRepository.findAll()).thenReturn(List.of(sampleItem));

        List<ItemResponse> responses = itemService.getAllItems();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Laptop", responses.get(0).name());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testGetAllItems_EmptyList() {
        when(itemRepository.findAll()).thenReturn(List.of());

        List<ItemResponse> responses = itemService.getAllItems();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(itemRepository, times(1)).findAll();
    }

    // ==========================================
    // 3. TESTS FOR updateItem
    // ==========================================

    @Test
    void testUpdateItem_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        when(itemRepository.save(any(Item.class))).thenReturn(sampleItem);

        ItemResponse response = itemService.updateItem(1L, sampleRequest);

        assertNotNull(response);
        assertEquals("Laptop", response.name());
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testUpdateItem_NotFound_ThrowsException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            itemService.updateItem(99L, sampleRequest);
        });

        assertEquals("Item not found", exception.getMessage());
        verify(itemRepository, never()).save(any(Item.class));
    }

    // ==========================================
    // 4. TESTS FOR patchItem
    // ==========================================

    @Test
    void testPatchItem_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        when(itemRepository.save(any(Item.class))).thenReturn(sampleItem);

        ItemResponse response = itemService.patchItem(1L, sampleRequest);

        assertNotNull(response);
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testPatchItem_NotFound_ThrowsException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            itemService.patchItem(99L, sampleRequest);
        });

        assertEquals("Item not found", exception.getMessage());
        verify(itemRepository, never()).save(any(Item.class));
    }

    // ==========================================
    // 5. TESTS FOR createItem
    // ==========================================

    @Test
    void testCreateItem_Success() {
        when(itemRepository.existsBySku("SKU-001")).thenReturn(false);
        when(itemRepository.save(any(Item.class))).thenReturn(sampleItem);

        ItemResponse response = itemService.createItem(sampleRequest);

        assertNotNull(response);
        assertEquals("SKU-001", response.sku());
        verify(itemRepository, times(1)).existsBySku("SKU-001");
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testCreateItem_SkuAlreadyExists_ThrowsException() {
        when(itemRepository.existsBySku("SKU-001")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.createItem(sampleRequest);
        });

        assertEquals("Item with SKU SKU-001 already exists.", exception.getMessage());
        verify(itemRepository, times(1)).existsBySku("SKU-001");
        verify(itemRepository, never()).save(any(Item.class));
    }
}