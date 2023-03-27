package com.mms.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Speciality;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.SpecialityRequest;
import com.mms.demo.model.SpecialityResponse;
import com.mms.demo.service.SpecialityService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/speciality")
public class SpecialityController {

    @Autowired
    SpecialityService specialityService;

    @GetMapping("/display")
    public ResponseEntity<List<SpecialityResponse>> displayAllSpecialities() {
        List<Speciality> specialities = specialityService.getAllSpecialities();
        List<SpecialityResponse> response = specialities.stream()
                        .map((s) -> SpecialityResponse.createResponseFromSpeciality(s))
                        .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<SpecialityResponse> getSpecialityById(@PathVariable Long id) {
        Speciality speciality = specialityService.getSpecialityById(id)
                        .orElseThrow(() -> new CustomException("Speciality with given id not found",
                                        "SPECIALITY_NOT_FOUND"));
        SpecialityResponse response = SpecialityResponse.createResponseFromSpeciality(speciality);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<SpecialityResponse> createSpeciality(
                    @Valid @RequestBody SpecialityRequest specialityRequest) {

        List<Speciality> specialities = specialityService.getAllSpecialities();
        Speciality alreadyCreatedSpeciality = specialities.stream()
                        .filter((s) -> s.getName().equals(specialityRequest.getName())).findFirst()
                        .orElse(null);

        if (alreadyCreatedSpeciality != null) {
            throw new CustomException("Speciality with given name already created",
                            "SPECIALITY_ALREADY_EXISTS");
        }

        Speciality speciality = SpecialityRequest.createSpecialityFromRequest(specialityRequest);
        Speciality createdSpeciality = specialityService.createSpeciality(speciality);
        SpecialityResponse response =
                        SpecialityResponse.createResponseFromSpeciality(createdSpeciality);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<SpecialityResponse> udpateSpeciality(@PathVariable Long id,
                    @Valid @RequestBody SpecialityRequest specialityRequest) {
        Speciality speciality = SpecialityRequest.createSpecialityFromRequest(specialityRequest);
        Speciality updatedSpeciality = specialityService.updateSpeciality(id, speciality);
        SpecialityResponse response =
                        SpecialityResponse.createResponseFromSpeciality(updatedSpeciality);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeciality(@PathVariable Long id) {
        specialityService.deleteSpeciality(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
