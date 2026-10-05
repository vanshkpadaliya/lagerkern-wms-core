package com.warehouse.lagerkern.catalog.infrastructure;

import com.warehouse.lagerkern.catalog.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import com.warehouse.lagerkern.TestcontainersConfiguration;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testExistsBySku_ReturnsTrueWhenSkuExists() {
        // --- 1. ARRANGE ---
        Item item = new Item("SKU-TEST-01", "Steel Pipe", "M", BigDecimal.valueOf(10.0), true);
        entityManager.persistAndFlush(item);

        // --- 2. ACT ---
        boolean exists = itemRepository.existsBySku("SKU-TEST-01");

        // --- 3. ASSERT ---
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsBySku_ReturnsFalseWhenSkuDoesNotExist() {
        // --- ACT ---
        boolean exists = itemRepository.existsBySku("SKU-NON-EXISTENT");

        // --- ASSERT ---
        assertThat(exists).isFalse();
    }

    @Test
    void testUniqueSkuConstraint_ThrowsException() {
        // --- 1. ARRANGE ---
        Item item1 = new Item("SKU-UNIQUE", "Primary Item", "PCS", BigDecimal.ONE, false);
        itemRepository.saveAndFlush(item1);

        // --- 2. ACT & ASSERT ---
        // Attempting to save a separate item with the exact same SKU should violate the database constraint
        Item item2 = new Item("SKU-UNIQUE", "Duplicate Item", "PCS", BigDecimal.ONE, false);

        assertThrows(DataIntegrityViolationException.class, () -> {
            itemRepository.saveAndFlush(item2);
        });
    }
}