package com.warehouse.lagerkern.catalog.api;

import com.warehouse.lagerkern.catalog.domain.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BatchRequestDTO(
        @NotNull(message = "Item ID is mandatory")
        Long itemId,

        @NotBlank(message = "Batch No is mandatory")
        String batchNo,

        LocalDate bestBefore
) {}
