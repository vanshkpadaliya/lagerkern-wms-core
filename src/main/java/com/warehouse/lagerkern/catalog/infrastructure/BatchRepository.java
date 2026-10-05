package com.warehouse.lagerkern.catalog.infrastructure;

import com.warehouse.lagerkern.catalog.domain.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

}
