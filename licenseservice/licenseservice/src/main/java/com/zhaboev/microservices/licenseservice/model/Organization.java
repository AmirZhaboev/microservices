package com.zhaboev.microservices.licenseservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Organization {
    private String organizationId;
    private String organizationName;
    private String organizationPhone;
    private String organizationEmail;
    private String organizationAddress;
    private String organizationCity;
}
