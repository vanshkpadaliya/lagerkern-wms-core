package com.warehouse.lagerkern.catalog.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ItemCreateRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @NotBlank String uom,
        @PositiveOrZero BigDecimal minStock,
        Boolean batchManaged
) {}