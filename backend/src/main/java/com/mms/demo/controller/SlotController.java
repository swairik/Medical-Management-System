package com.mms.demo.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Slot;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.SlotRequest;
import com.mms.demo.model.SlotResponse;
import com.mms.demo.service.SlotService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/slot")
public class SlotController {

    @Autowired
    SlotService slotService;

    @GetMapping("/display")
    public ResponseEntity<List<SlotResponse>> displayAllSlots() {
        List<Slot> slots = slotService.getAllSlots();
        List<SlotResponse> response = slots.stream().map((s) -> createResponseFromSlot(s)).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<SlotResponse> displaySlotById(@PathVariable Long id) {
        Slot slot = slotService.getSlotById(id)
                .orElseThrow(() -> new CustomException("Slot with given id not found", "SLOT_NOT_FOUND"));
        SlotResponse response = createResponseFromSlot(slot);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<SlotResponse> createSlot(@RequestBody SlotRequest slotRequest) {
        Slot slot = createSlotFromRequest(slotRequest);
        Slot createdSlot = slotService.createSlot(slot);
        SlotResponse response = createResponseFromSlot(createdSlot);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlotResponse> updateSlot(@PathVariable Long id, @Valid @RequestBody SlotRequest slotRequest) {
        Slot slot = createSlotFromRequest(slotRequest);
        Slot updatedSlot = slotService.updateSlot(id, slot);
        SlotResponse response = createResponseFromSlot(updatedSlot);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        slotService.deleteSlot(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public SlotResponse createResponseFromSlot(Slot slot) {
        SlotResponse slotResponse = new SlotResponse();
        BeanUtils.copyProperties(slot, slotResponse);
        return slotResponse;
    }

    public Slot createSlotFromRequest(SlotRequest slotRequest) {
        Slot slot = Slot.builder()
                .weekday(DayOfWeek.of(slotRequest.getWeekday()))
                .start(LocalTime.parse(slotRequest.getStart()))
                .end(LocalTime.parse(slotRequest.getEnd()))
                .capacity(slotRequest.getCapacity())
                .build();
        return slot;
    }
}
