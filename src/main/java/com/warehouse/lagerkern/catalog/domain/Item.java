package com.warehouse.lagerkern.catalog.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 20)
    private String uom; // Unit of Measure (e.g., PCS, KG, M)

    @Column(name = "min_stock", nullable = false, precision = 14, scale = 3)
    private BigDecimal minStock = BigDecimal.ZERO;

    @Column(name = "batch_managed", nullable = false)
    private boolean batchManaged = false;

    @Column(nullable = false)
    private boolean active = true;

    @Version
    private Long version;

    // Constructors
    public Item() {}

    public Item(String sku, String name, String uom, BigDecimal minStock, boolean batchManaged) {
        this.sku = sku;
        this.name = name;
        this.uom = uom;
        this.minStock = minStock != null ? minStock : BigDecimal.ZERO;
        this.batchManaged = batchManaged;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUom() { return uom; }
    public void setUom(String uom) { this.uom = uom; }

    public BigDecimal getMinStock() { return minStock; }
    public void setMinStock(BigDecimal minStock) { this.minStock = minStock; }

    public boolean isBatchManaged() { return batchManaged; }
    public void setBatchManaged(boolean batchManaged) { this.batchManaged = batchManaged; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}