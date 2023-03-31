package com.mms.demo.transferobject;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class SpecialityDTO {
    Long id;
    String name;
}
