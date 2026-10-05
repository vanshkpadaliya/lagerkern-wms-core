package com.warehouse.lagerkern.catalog.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BatchResponseDTO(
        Long id,

        Long itemId,

        String batchNo,

        LocalDate bestBefore
) {}
