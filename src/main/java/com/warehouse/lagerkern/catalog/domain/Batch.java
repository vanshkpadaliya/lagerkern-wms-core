package com.warehouse.lagerkern.catalog.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "batch", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_id", "batch_no"})
})
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "batch_no", nullable = false, length = 50)
    private String batchNo;

    @Column(name = "best_before")
    private LocalDate bestBefore;

    // Constructors
    public Batch() {}

    public Batch(Item item, String batchNo, LocalDate bestBefore) {
        this.item = item;
        this.batchNo = batchNo;
        this.bestBefore = bestBefore;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public LocalDate getBestBefore() { return bestBefore; }
    public void setBestBefore(LocalDate bestBefore) { this.bestBefore = bestBefore; }
}