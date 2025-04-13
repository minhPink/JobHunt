package com.minhdev.project.domain.response.resume;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateResumeDTO {
    private Long id;
    private Instant createdAt;
    private String createdBy;
}
