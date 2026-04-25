package com.zhaboev.microservices.organizationservice.service;

import com.zhaboev.microservices.organizationservice.datalayer.model.Organization;

public interface OrganizationService {
    Organization createOrganization(Organization organization);

    Organization getOrganization(String organizationId);

    Organization updateOrganization(String organizationId, Organization organization);

    void deleteOrganization(String organizationId);

}
