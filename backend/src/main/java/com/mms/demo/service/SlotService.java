package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Slot;

public interface SlotService {
    List<Slot> getAllSlots();
    Optional<Slot> getSlotById(Long id);

    Slot createSlot(Slot slot);
    Slot updateSlot(Long id, Slot slot);
    void deleteSlot(Long id);
}
