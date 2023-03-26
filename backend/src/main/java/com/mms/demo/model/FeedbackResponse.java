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
public class FeedbackResponse {
    private Long id;
    private String contents;

    public static FeedbackResponse createResponseFromFeedback(Feedback feedback) {
        FeedbackResponse feedbackResponse = FeedbackResponse.builder()
        .id(feedback.getId())
        .contents(new String(Base64.getDecoder().decode(feedback.getContents())))
        .build();
        return feedbackResponse;
    }
}