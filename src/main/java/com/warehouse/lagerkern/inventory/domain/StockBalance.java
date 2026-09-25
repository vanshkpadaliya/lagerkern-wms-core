package com.warehouse.lagerkern.inventory.domain;

import com.warehouse.lagerkern.catalog.domain.Batch;
import com.warehouse.lagerkern.catalog.domain.Item;
import com.warehouse.lagerkern.locations.domain.StorageLocation;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stock_balance", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_id", "location_id", "batch_id"})
})
public class StockBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private StorageLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal qty = BigDecimal.ZERO;

    @Column(name = "reserved_qty", nullable = false, precision = 14, scale = 3)
    private BigDecimal reservedQty = BigDecimal.ZERO;

    @Version
    private Long version;

    // Constructors
    public StockBalance() {}

    public StockBalance(Item item, StorageLocation location, Batch batch, BigDecimal qty) {
        this.item = item;
        this.location = location;
        this.batch = batch;
        this.qty = qty != null ? qty : BigDecimal.ZERO;
        this.reservedQty = BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public StorageLocation getLocation() { return location; }
    public void setLocation(StorageLocation location) { this.location = location; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }

    public BigDecimal getQty() { return qty; }
    public void setQty(BigDecimal qty) { this.qty = qty; }

    public BigDecimal getReservedQty() { return reservedQty; }
    public void setReservedQty(BigDecimal reservedQty) { this.reservedQty = reservedQty; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}