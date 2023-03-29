// package com.mms.demo.controller;

// import java.time.DayOfWeek;
// import java.time.LocalTime;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.mms.demo.exception.CustomException;
// import com.mms.demo.model.SlotRequest;
// import com.mms.demo.model.SlotResponse;

// import jakarta.validation.Valid;

// @CrossOrigin("*")
// @RestController
// @RequestMapping("/slot")
// public class SlotController {

//     // @Autowired
//     // SlotService slotService;

//     private static final Integer SLOT_DURATION = 30;

//     // @GetMapping("/display")
//     // public ResponseEntity<List<SlotResponse>> displayAllSlots() {
//     //     List<Slot> slots = slotService.getAllSlots();
//     //     List<SlotResponse> response =
//     //                     slots.stream().map((s) -> SlotResponse.createResponseFromSlot(s))
//     //                                     .collect(Collectors.toList());
//     //     return new ResponseEntity<>(response, HttpStatus.OK);
//     // }

//     // @GetMapping("/display/{id}")
//     // public ResponseEntity<SlotResponse> displaySlotById(@PathVariable Long id) {
//     //     Slot slot = slotService.getSlotById(id)
//     //                     .orElseThrow(() -> new CustomException("Slot with given id not found",
//     //                                     "SLOT_NOT_FOUND"));
//     //     SlotResponse response = SlotResponse.createResponseFromSlot(slot);
//     //     return new ResponseEntity<>(response, HttpStatus.OK);
//     // }

//     // @PostMapping("/")
//     // public ResponseEntity<List<SlotResponse>> createSlots(@RequestBody SlotRequest slotRequest) {
//     //     List<Slot> slots = createAllSlotsFromRequest(slotRequest);
//     //     List<SlotResponse> response = new ArrayList<>();
//     //     for (Slot slot : slots) {
//     //         Slot createdSlot = slotService.createSlot(slot);
//     //         response.add(SlotResponse.createResponseFromSlot(createdSlot));
//     //     }
//     //     return new ResponseEntity<>(response, HttpStatus.CREATED);
//     // }

//     // @PutMapping("/{id}")
//     // public ResponseEntity<SlotResponse> updateSlot(@PathVariable Long id,
//     //                 @Valid @RequestBody SlotRequest slotRequest) {
//     //     slotService.getSlotById(id)
//     //                     .orElseThrow(() -> new CustomException("Slot with given id not found",
//     //                                     "SLOT_NOT_FOUND"));

//     //     Slot updateslot = createSlotFromRequest(slotRequest);
//     //     Slot updatedSlot = slotService.updateSlot(id, updateslot);
//     //     SlotResponse response = SlotResponse.createResponseFromSlot(updatedSlot);
//     //     return new ResponseEntity<>(response, HttpStatus.OK);
//     // }

//     // @DeleteMapping("/{id}")
//     // public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
//     //     slotService.deleteSlot(id);
//     //     return new ResponseEntity<>(HttpStatus.OK);
//     // }

//     // public List<Slot> createAllSlotsFromRequest(SlotRequest slotRequest) {
//     //     LocalTime start = LocalTime.parse(slotRequest.getStart());
//     //     LocalTime end = LocalTime.parse(slotRequest.getEnd());

//     //     if (start.until(end, ChronoUnit.MINUTES) < 0) {
//     //         throw new CustomException("End time is less than start time",
//     //                         "END_TIME_LESS_THAN_START_TIME");
//     //     }

//     //     if (start.until(end, ChronoUnit.MINUTES) < SLOT_DURATION) {
//     //         throw new CustomException(String.format(
//     //                         "End time and start time need a minimum difference of %s minutes",
//     //                         SLOT_DURATION), "NOT_ENOUGH_TIME_BETWEEN_START_AND_END");
//     //     }

//     //     Integer capacity = (int) start.until(end, ChronoUnit.MINUTES) / SLOT_DURATION;

//     //     List<Slot> slots = new ArrayList<>();

//     //     for (int i = 0; i < capacity; i++) {
//     //         slots.add(createSlotFromRequest(DayOfWeek.of(slotRequest.getWeekday()),
//     //                         start.plusMinutes(i * SLOT_DURATION),
//     //                         start.plusMinutes((i + 1) * SLOT_DURATION)));
//     //     }

//     //     return slots;
//     // }

//     // public Slot createSlotFromRequest(DayOfWeek weekDay, LocalTime start, LocalTime end) {
//     //     if (start.until(end, ChronoUnit.MINUTES) != SLOT_DURATION) {
//     //         throw new CustomException(String.format(
//     //                         "End time and start time has to have a difference of %s minutes",
//     //                         SLOT_DURATION), "NOT_ENOUGH_TIME_BETWEEN_START_AND_END");
//     //     }

//     //     Slot slot = Slot.builder().weekday(weekDay).start(start).end(end).build();
//     //     return slot;
//     // }

//     // public Slot createSlotFromRequest(SlotRequest slotRequest) {
//     //     LocalTime start = LocalTime.parse(slotRequest.getStart());
//     //     LocalTime end = LocalTime.parse(slotRequest.getEnd());

//     //     if (start.until(end, ChronoUnit.MINUTES) != SLOT_DURATION) {
//     //         throw new CustomException(String.format(
//     //                         "End time and start time has to have a difference of %s minutes",
//     //                         SLOT_DURATION), "NOT_ENOUGH_TIME_BETWEEN_START_AND_END");
//     //     }

//     //     Slot slot = Slot.builder().weekday(DayOfWeek.of(slotRequest.getWeekday())).start(start)
//     //                     .end(end).build();
//     //     return slot;
//     // }

// }
