package com.warehouse.lagerkern.inventory.domain;

import com.warehouse.lagerkern.catalog.domain.Batch;
import com.warehouse.lagerkern.catalog.domain.Item;
import com.warehouse.lagerkern.locations.domain.StorageLocation;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private MovementType movementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private StorageLocation fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private StorageLocation toLocation;

    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal qty;

    @Column(length = 255)
    private String reason;

    @Column(name = "ref_type", length = 50)
    private String refType;

    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "created_by", length = 100)
    private String createdBy;

    public enum MovementType {
        RECEIPT, PUTAWAY, PICK, ADJUST, TRANSFER
    }

    // Constructors
    public StockMovement() {}

    public StockMovement(MovementType movementType, Item item, Batch batch,
                         StorageLocation fromLocation, StorageLocation toLocation,
                         BigDecimal qty, String reason, String refType, Long refId, String idempotencyKey, String createdBy) {
        this.movementType = movementType;
        this.item = item;
        this.batch = batch;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.qty = qty;
        this.reason = reason;
        this.refType = refType;
        this.refId = refId;
        this.idempotencyKey = idempotencyKey;
        this.createdBy = createdBy;
    }

    // Getters (Note: No setters for immutable ledger entries)
    public Long getId() { return id; }
    public MovementType getMovementType() { return movementType; }
    public Item getItem() { return item; }
    public Batch getBatch() { return batch; }
    public StorageLocation getFromLocation() { return fromLocation; }
    public StorageLocation getToLocation() { return toLocation; }
    public BigDecimal getQty() { return qty; }
    public String getReason() { return reason; }
    public String getRefType() { return refType; }
    public Long getRefId() { return refId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }
}