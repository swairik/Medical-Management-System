package com.mms.demo.transferobject;



import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@Builder
public class ReportDTO {
    private final @NonNull Long id;
    private final @NonNull String filename;
    private final @NonNull byte[] contents;
    private final @NonNull Integer contentLength;
}
