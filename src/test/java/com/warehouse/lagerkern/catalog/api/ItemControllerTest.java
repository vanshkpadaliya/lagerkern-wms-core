package com.warehouse.lagerkern.catalog.api;

import com.warehouse.lagerkern.catalog.application.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // ✅
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean; // ✅
// and change your annotation usages to @MockBean
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    // Sample request payload matching ItemCreateRequest
    private final ItemCreateRequest requestDTO = new ItemCreateRequest(
            "SKU-12345",
            "Laptop",
            "PCS",
            BigDecimal.valueOf(10),
            true
    );

    // Sample response payload matching ItemResponse
    private final ItemResponse responseDTO = new ItemResponse(
            1L,
            "SKU-12345",
            "Laptop",
            "PCS",
            BigDecimal.valueOf(10),
            true,
            true
    );

    // ==========================================
    // 1. TESTS FOR POST /api/v1/items
    // ==========================================

    @Test
    void testCreateItem_Success() throws Exception {
        when(itemService.createItem(any(ItemCreateRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated()) // HTTP 201 Created
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.sku").value("SKU-12345"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.uom").value("PCS"))
                .andExpect(jsonPath("$.minStock").value(10))
                .andExpect(jsonPath("$.batchManaged").value(true))
                .andExpect(jsonPath("$.active").value(true));
    }

    // ==========================================
    // 2. TESTS FOR GET /api/v1/items/{id}
    // ==========================================

    @Test
    void testGetItemById_Success() throws Exception {
        when(itemService.getItemById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/items/1"))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.sku").value("SKU-12345"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.uom").value("PCS"));
    }

    // ==========================================
    // 3. TESTS FOR GET /api/v1/items
    // ==========================================

    @Test
    void testGetAllItems_Success() throws Exception {
        when(itemService.getAllItems()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/items"))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].sku").value("SKU-12345"));
    }

    // ==========================================
    // 4. TESTS FOR PUT /api/v1/items/{id}
    // ==========================================

    @Test
    void testUpdateItem_Success() throws Exception {
        when(itemService.updateItem(eq(1L), any(ItemCreateRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/items/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    // ==========================================
    // 5. TESTS FOR PATCH /api/v1/items/{id}
    // ==========================================

    @Test
    void testPatchItem_Success() throws Exception {
        when(itemService.patchItem(eq(1L), any(ItemCreateRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/items/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }
}