CREATE TABLE batch (
                       id BIGSERIAL PRIMARY KEY,
                       item_id BIGINT NOT NULL,
                       batch_no VARCHAR(50) NOT NULL,
                       best_before DATE,
                       CONSTRAINT fk_batch_item FOREIGN KEY (item_id) REFERENCES item(id),
                       CONSTRAINT uk_item_batch UNIQUE (item_id, batch_no)
);

CREATE TABLE storage_location (
                                  id BIGSERIAL PRIMARY KEY,
                                  warehouse_id BIGINT NOT NULL,
                                  code VARCHAR(50) NOT NULL,
                                  zone VARCHAR(50) NOT NULL,
                                  type VARCHAR(20) NOT NULL,
                                  max_qty NUMERIC(14, 3),
                                  active BOOLEAN NOT NULL DEFAULT TRUE,
                                  version BIGINT,
                                  CONSTRAINT fk_location_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id),
                                  CONSTRAINT uk_warehouse_code UNIQUE (warehouse_id, code)
);

CREATE TABLE stock_balance (
                               id BIGSERIAL PRIMARY KEY,
                               item_id BIGINT NOT NULL,
                               location_id BIGINT NOT NULL,
                               batch_id BIGINT,
                               qty NUMERIC(14, 3) NOT NULL DEFAULT 0,
                               reserved_qty NUMERIC(14, 3) NOT NULL DEFAULT 0,
                               version BIGINT,
                               CONSTRAINT fk_stock_item FOREIGN KEY (item_id) REFERENCES item(id),
                               CONSTRAINT fk_stock_location FOREIGN KEY (location_id) REFERENCES storage_location(id),
                               CONSTRAINT fk_stock_batch FOREIGN KEY (batch_id) REFERENCES batch(id),
                               CONSTRAINT uk_stock_item_location_batch UNIQUE (item_id, location_id, batch_id)
);

CREATE TABLE stock_movement (
                                id BIGSERIAL PRIMARY KEY,
                                movement_type VARCHAR(30) NOT NULL,
                                item_id BIGINT NOT NULL,
                                batch_id BIGINT,
                                from_location_id BIGINT,
                                to_location_id BIGINT,
                                qty NUMERIC(14, 3) NOT NULL,
                                reason VARCHAR(255),
                                ref_type VARCHAR(50),
                                ref_id BIGINT,
                                idempotency_key VARCHAR(100) UNIQUE,
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                created_by VARCHAR(100),
                                CONSTRAINT fk_movement_item FOREIGN KEY (item_id) REFERENCES item(id),
                                CONSTRAINT fk_movement_batch FOREIGN KEY (batch_id) REFERENCES batch(id),
                                CONSTRAINT fk_movement_from_loc FOREIGN KEY (from_location_id) REFERENCES storage_location(id),
                                CONSTRAINT fk_movement_to_loc FOREIGN KEY (to_location_id) REFERENCES storage_location(id)
);
