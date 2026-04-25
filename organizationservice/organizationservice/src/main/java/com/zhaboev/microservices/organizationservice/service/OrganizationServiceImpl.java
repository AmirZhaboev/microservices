package com.zhaboev.microservices.organizationservice.service;

import org.springframework.stereotype.Service;

import com.zhaboev.microservices.organizationservice.datalayer.model.Organization;
import com.zhaboev.microservices.organizationservice.datalayer.repository.OrganizationRepository;
import com.zhaboev.microservices.organizationservice.exception.OrganizationNotFoundException;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public Organization getOrganization(String organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found: " + organizationId));
    }

    @Override
    public Organization updateOrganization(String organizationId, Organization newOrganization) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found: " + organizationId));

        organization.setOrganizationName(newOrganization.getOrganizationName());
        organization.setOrganizationPhone(newOrganization.getOrganizationPhone());
        organization.setOrganizationEmail(newOrganization.getOrganizationEmail());
        organization.setOrganizationAddress(newOrganization.getOrganizationAddress());
        organization.setOrganizationCity(newOrganization.getOrganizationCity());

        return organizationRepository.save(organization);
    }

    @Override
    public void deleteOrganization(String organizationId) {
        organizationRepository.delete(organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found: " + organizationId)));
    }

}
