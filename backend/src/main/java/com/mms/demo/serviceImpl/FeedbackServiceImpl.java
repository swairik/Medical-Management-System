package com.mms.demo.serviceImpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mms.demo.entity.Feedback;
import com.mms.demo.repository.FeedbackRepository;
import com.mms.demo.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    FeedbackRepository repository;

    @Override
    public Optional<Feedback> create(Feedback feedback) {
        if (feedback == null) {
            return Optional.empty();
        }

        return Optional.of(repository.save(feedback));
    }

    @Override
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    @Override
    public Optional<Feedback> getById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return repository.findById(id);
    }

    @Override
    public Optional<Feedback> update(Long id, Feedback updates) {
        if (id == null) {
            return Optional.empty();
        }

        Optional<Feedback> fetchedContainer = repository.findById(id);
        if (fetchedContainer.isEmpty()) {
            return Optional.empty();
        }

        Feedback feedback = fetchedContainer.get();
        feedback.setContents(updates.getContents());

        return Optional.of(repository.save(feedback));
    }

}
