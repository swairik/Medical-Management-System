package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mms.demo.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
