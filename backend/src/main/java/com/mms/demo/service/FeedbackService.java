package com.mms.demo.service;

import java.util.Optional;
import com.mms.demo.entity.Feedback;

public interface FeedbackService {
    Optional<Feedback> getById(Long id);

    @Deprecated
    Optional<Feedback> create(Feedback feedback);

    Optional<Feedback> update(Long id, Feedback updates);

    void delete(Long id);
}
