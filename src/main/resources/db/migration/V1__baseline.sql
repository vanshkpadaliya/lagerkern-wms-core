-- Warehouse and Item Master baseline for Lagerkern
CREATE TABLE warehouse (
                           id BIGSERIAL PRIMARY KEY,
                           code VARCHAR(50) UNIQUE NOT NULL,
                           name VARCHAR(100) NOT NULL,
                           created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item (
                      id BIGSERIAL PRIMARY KEY,
                      sku VARCHAR(50) UNIQUE NOT NULL,
                      name VARCHAR(150) NOT NULL,
                      uom VARCHAR(20) NOT NULL,
                      min_stock NUMERIC(14,3) DEFAULT 0,
                      active BOOLEAN DEFAULT TRUE,
                      version BIGINT DEFAULT 0
)