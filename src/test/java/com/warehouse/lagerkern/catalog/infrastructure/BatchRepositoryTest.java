package com.warehouse.lagerkern.catalog.infrastructure;

import com.warehouse.lagerkern.catalog.domain.Batch;
import com.warehouse.lagerkern.catalog.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.warehouse.lagerkern.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class BatchRepositoryTest {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testSaveAndFindById_Success() {
        // --- 1. ARRANGE ---
        Item item = new Item(
                "TEST-SKU-001",
                "Test Item",
                "PCS",
                BigDecimal.ZERO,
                true
        );

        Item savedItem = itemRepository.save(item);


        Batch batch = new Batch( savedItem, "BATCH-001", LocalDate.now().plusMonths(6) );
        // --- 2. ACT ---
        // Save directly via the repository, which returns the saved entity with a generated ID
        Batch savedBatch = batchRepository.save(batch);
        Long generatedId = savedBatch.getId();

        Optional<Batch> foundBatch = batchRepository.findById(generatedId);

        // --- 3. ASSERT ---
        assertThat(foundBatch).isPresent();
        assertThat(foundBatch.get().getId()).isEqualTo(generatedId);
    }
}