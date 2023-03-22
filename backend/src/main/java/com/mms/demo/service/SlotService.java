package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.entity.Slot;

/**
 * The Interface SlotService defines all the interactions needed between a high level controller and
 * the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface SlotService {
    /**
     * Gets all slots.
     *
     * @return the list of all slots
     */
    List<Slot> getAllSlots();

    /**
     * Gets the slot by id.
     *
     * @param id the id
     * @return the slot by id
     */
    Optional<Slot> getSlotById(Long id);

    /**
     * Creates the slot.
     *
     * @param slot the slot
     * @return the slot
     */
    Slot createSlot(Slot slot);

    /**
     * Update slot.
     *
     * @param id the id
     * @param slot the slot
     * @return the slot
     */
    Slot updateSlot(Long id, Slot slot);

    /**
     * Delete slot.
     *
     * @param id the id
     */
    void deleteSlot(Long id);
}
