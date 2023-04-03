package com.mms.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.mms.demo.exception.CustomException;
import com.mms.demo.service.SpecialityService;
import com.mms.demo.transferobject.SpecialityDTO;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/speciality")
public class SpecialityController {

    @Autowired
    SpecialityService specialityService;

    @GetMapping("/display")
    public ResponseEntity<List<SpecialityDTO>> displayAllSpecialities() {
        List<SpecialityDTO> specialitiesList = specialityService.getAll();
        return new ResponseEntity<>(specialitiesList, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<SpecialityDTO> getSpecialityById(@PathVariable Long id) {
        SpecialityDTO speciality = specialityService.get(id)
                .orElseThrow(() -> new CustomException("Speciality with given id not found", "SPECIALITY_NOT_FOUND",
                        HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(speciality, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<SpecialityDTO> createSpeciality(@Valid @RequestBody SpecialityDTO specialityRequest) {

        List<SpecialityDTO> specialityList = specialityService.getAll();
        SpecialityDTO alreadyCreatedSpeciality = specialityList.stream()
                .filter((s) -> s.getName().equals(specialityRequest.getName())).findFirst().orElse(null);

        if (alreadyCreatedSpeciality != null) {
            throw new CustomException("Speciality with given name already created",
                    "SPECIALITY_ALREADY_EXISTS", HttpStatus.NOT_FOUND);
        }

        SpecialityDTO createdSpeciality = specialityService.create(specialityRequest);
        return new ResponseEntity<>(createdSpeciality, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialityDTO> udpateSpeciality(@PathVariable Long id,
            @Valid @RequestBody SpecialityDTO specialityRequest) {
        SpecialityDTO updatedSpeciality = specialityService.update(id, specialityRequest)
                .orElseThrow(() -> new CustomException("Error while updating", "SPECIALITY_NOT_UPDATED",
                        HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(updatedSpeciality, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeciality(@PathVariable Long id) {
        specialityService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
