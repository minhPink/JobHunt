package com.minhdev.project.domain.response;

import com.minhdev.project.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private CompanyUser companyUser;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
