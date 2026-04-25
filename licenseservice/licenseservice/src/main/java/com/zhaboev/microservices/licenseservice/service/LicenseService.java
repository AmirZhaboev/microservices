package com.zhaboev.microservices.licenseservice.service;

import com.zhaboev.microservices.licenseservice.DTO.LicenseDTO;
import com.zhaboev.microservices.licenseservice.model.License;

public interface LicenseService {
    public License getLicense(String licenseId, String organizationId);

    public void deleteLicense(String licenseId);

    public License createLicense(License license);

    public LicenseDTO getLicenseDto(String licenseId, String organizationId);

    public LicenseDTO createLicenseDto(License license);
}