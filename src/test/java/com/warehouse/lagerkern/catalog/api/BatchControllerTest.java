package com.warehouse.lagerkern.catalog.api;

import com.warehouse.lagerkern.catalog.application.BatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.lagerkern.catalog.domain.exception.BatchNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
@WebMvcTest(BatchController.class)
public class BatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatchService batchService;

    @Autowired
    private ObjectMapper objectMapper;

    // Sample request data matching your DTO structures
    private final BatchRequestDTO requestDTO = new BatchRequestDTO(
            1L,
            "97865434567",
            LocalDate.of(2023, 4, 12)
    );

    // Sample response data matching your DTO structures
    private final BatchResponseDTO responseDTO = new BatchResponseDTO(
            6L,
            1L,
            "97865434567",
            LocalDate.of(2023, 4, 12)
    );

    // ==========================================
    // 1. TESTS FOR POST /api/v1/batches
    // ==========================================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateBatch_Success() throws Exception {
        // Arrange
        when(batchService.createBatch(any(BatchRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/batches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated()) // HTTP 201 Created
                .andExpect(jsonPath("$.id").value(6L))
                .andExpect(jsonPath("$.itemId").value(1L))
                .andExpect(jsonPath("$.batchNo").value("97865434567"))
                .andExpect(jsonPath("$.bestBefore").value("2023-04-12"));
    }

    // ==========================================
    // 2. TESTS FOR GET /api/v1/batches/{id}
    // ==========================================

    @Test
    void testGetBatchById_Success() throws Exception {
        // Arrange
        when(batchService.getBatchById(6L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/batches/6"))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.id").value(6L))
                .andExpect(jsonPath("$.itemId").value(1L))
                .andExpect(jsonPath("$.batchNo").value("97865434567"))
                .andExpect(jsonPath("$.bestBefore").value("2023-04-12"));
    }

    @Test
    void testGetBatchById_NotFound() throws Exception {
        // Arrange
        when(batchService.getBatchById(99L)).thenThrow(new BatchNotFoundException(99L));

        // Act & Assert
        mockMvc.perform(get("/api/v1/batches/99"))
                .andExpect(status().isNotFound()); // Captures unhandled runtime exceptions
    }

    // ==========================================
    // 3. TESTS FOR GET /api/v1/batches
    // =========================================
    @Test
    void testGetAllBatches_Success() throws Exception {
        // Arrange
        when(batchService.getAllBatches()).thenReturn(List.of(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/api/v1/batches"))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$").isArray()) // Verifies root response is a JSON array
                .andExpect(jsonPath("$[0].id").value(6L))
                .andExpect(jsonPath("$[0].itemId").value(1L))
                .andExpect(jsonPath("$[0].batchNo").value("97865434567"));
    }
}