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
public class SpecialityDTO {
    private final @NonNull Long id;
    private final @NonNull String name;
}
