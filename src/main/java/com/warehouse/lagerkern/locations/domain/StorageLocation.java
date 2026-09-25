package com.warehouse.lagerkern.locations.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "storage_location", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"warehouse_id", "code"})
})
public class StorageLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 50)
    private String zone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LocationType type;

    @Column(name = "max_qty", precision = 14, scale = 3)
    private BigDecimal maxQty;

    @Column(nullable = false)
    private boolean active = true;

    @Version
    private Long version;

    public enum LocationType {
        BIN, DOCK, BULK
    }

    // Constructors
    public StorageLocation() {}

    public StorageLocation(Warehouse warehouse, String code, String zone, LocationType type, BigDecimal maxQty) {
        this.warehouse = warehouse;
        this.code = code;
        this.zone = zone;
        this.type = type;
        this.maxQty = maxQty;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public LocationType getType() { return type; }
    public void setType(LocationType type) { this.type = type; }

    public BigDecimal getMaxQty() { return maxQty; }
    public void setMaxQty(BigDecimal maxQty) { this.maxQty = maxQty; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}