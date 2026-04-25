package com.zhaboev.microservices.licenseservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.zhaboev.microservices.licenseservice.DTO.LicenseDTO;
import com.zhaboev.microservices.licenseservice.client.OrganizationClient;
import com.zhaboev.microservices.licenseservice.controller.DTOmapper;
import com.zhaboev.microservices.licenseservice.exception.OrganizationNotFoundClientException;
import com.zhaboev.microservices.licenseservice.model.License;
import com.zhaboev.microservices.licenseservice.model.Organization;
import com.zhaboev.microservices.licenseservice.repository.LicenseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final OrganizationClient organizationClient;
    private final DTOmapper dtoMapper;

    @Override
    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new IllegalArgumentException("Нет лицензии с таким ID"));

        if (!organizationId.equals(license.getOrganizationId())) {
            throw new IllegalArgumentException("Лицензия не принадлежит данной организации");
        }

        return license;
    }

    @Override
    public LicenseDTO getLicenseDto(String licenseId, String organizationId) {
        License license = getLicense(licenseId, organizationId);

        Organization organization;
        try {
            organization = organizationClient.getOrganization(organizationId);
        } catch (OrganizationNotFoundClientException e) {
            organization = null;
        }

        return dtoMapper.toDTO(license, organization);
    }

    @Override
    public LicenseDTO createLicenseDto(License license) {

        License saved = createLicense(license);
        Organization organization;
        try {
            organization = organizationClient.getOrganization(saved.getOrganizationId());
        } catch (OrganizationNotFoundClientException e) {
            organization = null;
        }

        return dtoMapper.toDTO(saved, organization);
    }

    @Override
    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        return licenseRepository.save(license);
    }

    @Override
    public void deleteLicense(String licenseId) {
        licenseRepository.deleteById(licenseId);
    }

}
