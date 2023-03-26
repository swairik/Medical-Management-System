package com.mms.demo.model;

import java.util.Base64;

import com.mms.demo.entity.Feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequest {
    private String contents;

    public static Feedback createFeedbackFromRequest(FeedbackRequest feedbackRequest) {
        Feedback feedback = Feedback.builder()
                .contents(Base64.getEncoder().encode(feedbackRequest.getContents().getBytes()))
                .build();
        return feedback;
    }

}
