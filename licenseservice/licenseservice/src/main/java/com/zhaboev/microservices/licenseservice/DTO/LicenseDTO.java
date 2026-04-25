package com.zhaboev.microservices.licenseservice.DTO;

import com.zhaboev.microservices.licenseservice.model.Organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LicenseDTO {
    private String licenseId;
    private String description;
    private String productName;
    private String licenseType;
    private String comment;
    private Organization organization;
}
