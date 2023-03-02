package com.mms.demo.slot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mms.demo.entity.Slot;
import com.mms.demo.service.SlotService;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.DayOfWeek;
import java.time.LocalTime;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class SlotServiceTest {
    @Autowired
    SlotService impl;

    static final Slot slot = Slot.builder().start(LocalTime.of(0, 0, 0)).end(LocalTime.of(0, 0, 1)).weekday(DayOfWeek.MONDAY).capacity(5).build();

    @Order(1)
    @Test
    @DisplayName("Testing create on a single slot")
    void testCreateSlot() {
        assertThat(impl.createSlot(slot)).isEqualTo(slot);
    }
 
    @Order(2)
    @Test
    @DisplayName("Testing fetch on all slots")
    void testGetAllSlots() {
        assertThat(impl.getAllSlots()).isNotEmpty().contains(slot);
    }

    @Order(3)
    @Test
    @DisplayName("Testing fetch on a single slot by id")
    void testGetSlotById() {
        assertThat(impl.getSlotById(slot.getId())).isNotEmpty().contains(slot);
    }

    @Order(4)
    @Test
    @DisplayName("Testing update on a single slot by id")
    void testUpdateSlot() {
        Slot tempSlot = slot.toBuilder().capacity(2).build();
        assertThat(impl.updateSlot(slot.getId(), tempSlot)).isEqualTo(tempSlot).isNotEqualTo(slot);
    }

    @Order(5)
    @Test
    @DisplayName("Testing delete on a single slot by id")
    void testDeleteSlot() {
        impl.deleteSlot(slot.getId());
        assertThat(impl.getSlotById(slot.getId())).isEmpty();
    }
}
