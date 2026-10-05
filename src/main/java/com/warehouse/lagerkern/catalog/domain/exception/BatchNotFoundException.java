package com.warehouse.lagerkern.catalog.domain.exception;

public class BatchNotFoundException extends RuntimeException {
    public BatchNotFoundException(Long id) {
        super("Could not find batch with id: " + id);
    }
}
