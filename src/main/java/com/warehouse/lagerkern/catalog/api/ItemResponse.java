package com.warehouse.lagerkern.catalog.api;

import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        String sku,
        String name,
        String uom,
        BigDecimal minStock,
        boolean batchManaged,
        boolean active
) {}