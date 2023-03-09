package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Slot;
import com.mms.demo.repository.SlotRepository;
import com.mms.demo.service.SlotService;

@Service
public class SlotServiceImpl implements SlotService {
    @Autowired
    private SlotRepository slotRepo;

    @Override
    public Slot createSlot(Slot slot) {
        if (slot.getStart().isAfter(slot.getEnd())) {
            throw new IllegalArgumentException("Start time should be before End time");
        }

        return slotRepo.save(slot);
    }

    @Override
    public void deleteSlot(Long id) {
        slotRepo.deleteById(id);
    }

    @Override
    public List<Slot> getAllSlots() {
        return slotRepo.findAll();
    }

    @Override
    public Optional<Slot> getSlotById(Long id) {
        return slotRepo.findById(id);
    }

    @Override
    public Slot updateSlot(Long id, Slot slotUpdates) {
        Optional<Slot> temp = slotRepo.findById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Slot slot = temp.get();
        slot.setCapacity(slotUpdates.getCapacity());
        slot.setEnd(slotUpdates.getEnd());
        slot.setStart(slotUpdates.getStart());
        slot.setWeekday(slotUpdates.getWeekday());

        return slotRepo.save(slot);
    }

    
}
