package com.mms.demo.service;

import java.util.List;
import java.util.Optional;

import com.mms.demo.transferobject.SpecialityDTO;

/**
 * The Interface SpecialityService defines all the interactions needed between a
 * high level
 * controller and the corresponding repository
 * 
 * @author Mananveer Singh
 */
public interface SpecialityService {
    /**
     * Gets the speciality by id.
     *
     * @param id the id
     * @return the speciality by id
     */
    Optional<SpecialityDTO> get(Long id);

    /**
     * Gets the all specialities.
     *
     * @return the list of all specialities
     */
    List<SpecialityDTO> getAll();

    /**
     * Creates the speciality.
     *
     * @param speciality the speciality
     * @return the speciality
     */
    SpecialityDTO create(SpecialityDTO speciality);

    /**
     * Delete speciality.
     *
     * @param id the id
     */
    void delete(Long id);

    /**
     * Update speciality.
     *
     * @param id                the id
     * @param specialityUpdates the speciality updates
     * @return the speciality
     */
    Optional<SpecialityDTO> update(Long id, SpecialityDTO specialityUpdates);
}
