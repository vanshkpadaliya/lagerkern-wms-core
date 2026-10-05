package com.warehouse.lagerkern.catalog.application;

import com.warehouse.lagerkern.catalog.api.ItemCreateRequest;
import com.warehouse.lagerkern.catalog.api.ItemResponse;
import com.warehouse.lagerkern.catalog.domain.Item;
import com.warehouse.lagerkern.catalog.infrastructure.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemResponse getItemById(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return mapToDto(item);
    }

    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private ItemResponse mapToDto(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getSku(),
                item.getName(),
                item.getUom(),
                item.getMinStock(),
                item.isBatchManaged(),
                item.isActive()
        );
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemCreateRequest request) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        existingItem.setName(request.name());
        existingItem.setSku(request.sku());
        existingItem.setMinStock(request.minStock());
        existingItem.setUom(request.uom());
        existingItem.setBatchManaged(request.batchManaged());

        Item savedItem = itemRepository.save(existingItem);

        return mapToDto(savedItem);
    }

    @Transactional
    public ItemResponse patchItem(Long id, ItemCreateRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if(request.name() != null)
        {
            item.setName(request.name());
        }

        if(request.sku() != null)
        {
            item.setSku(request.sku());
        }

        if(request.minStock() != null)
        {
            item.setMinStock(request.minStock());
        }

        if(request.uom() != null)
        {
            item.setUom(request.uom());
        }

        if(request.batchManaged() != null)
        {
            item.setBatchManaged(request.batchManaged());
        }

        Item savedItem = itemRepository.save(item);
        return mapToDto(savedItem);
    }

    @Transactional
    public ItemResponse createItem(ItemCreateRequest request) {
        if (itemRepository.existsBySku(request.sku())) {
            throw new IllegalArgumentException("Item with SKU " + request.sku() + " already exists.");
        }

        // Uses the exact constructor defined in your Item.java entity
        Item item = new Item(
                request.sku(),
                request.name(),
                request.uom(),
                request.minStock(),
                request.batchManaged()
        );

        Item saved = itemRepository.save(item);

        return new ItemResponse(
                saved.getId(),
                saved.getSku(),
                saved.getName(),
                saved.getUom(),
                saved.getMinStock(),
                saved.isBatchManaged(),
                saved.isActive()
        );
    }
}