package com.minhdev.project.domain.response.file;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUploadFileDTO {
    private String fileName;
    private Instant uploadedAt;
}
