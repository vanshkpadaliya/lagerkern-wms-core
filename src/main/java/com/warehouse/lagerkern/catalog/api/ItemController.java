package com.warehouse.lagerkern.catalog.api;

import com.warehouse.lagerkern.catalog.application.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items") //[cite: 1]
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemCreateRequest request) {
        ItemResponse response = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); //[cite: 1]
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse>  getItem(@PathVariable Long id) {
        ItemResponse itemResponse = itemService.getItemById(id);
        return ResponseEntity.ok().body(itemResponse);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok().body(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemCreateRequest request) {
        ItemResponse updatedItem = itemService.updateItem(id, request);
        return  ResponseEntity.ok().body(updatedItem);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse> patchItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemCreateRequest request
    ) {
        ItemResponse patchedItem = itemService.patchItem(id, request);
        return ResponseEntity.ok().body(patchedItem);
    }
}