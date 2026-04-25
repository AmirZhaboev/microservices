package com.zhaboev.microservices.licenseservice.controller;

import org.springframework.stereotype.Component;

import com.zhaboev.microservices.licenseservice.DTO.LicenseDTO;
import com.zhaboev.microservices.licenseservice.model.License;
import com.zhaboev.microservices.licenseservice.model.Organization;

@Component
public class DTOmapper {
    public LicenseDTO toDTO(License license, Organization organization) {
        LicenseDTO licenseDTO = new LicenseDTO();
        licenseDTO.setProductName(license.getProductName());
        licenseDTO.setDescription(license.getDescription());
        licenseDTO.setComment(license.getComment());
        licenseDTO.setLicenseId(license.getLicenseId());
        licenseDTO.setLicenseType(license.getLicenseType());
        licenseDTO.setOrganization(organization);

        return licenseDTO;
    }

    public License toLicense(LicenseDTO licenseDTO) {
        License license = new License();

        license.setComment(licenseDTO.getComment());
        license.setDescription(licenseDTO.getDescription());
        license.setLicenseId(licenseDTO.getLicenseId());
        license.setLicenseType(licenseDTO.getLicenseType());
        license.setProductName(licenseDTO.getProductName());

        if (licenseDTO.getOrganization() != null) {
            license.setOrganizationId(licenseDTO.getOrganization().getOrganizationId());
        }

        return license;
    }

}