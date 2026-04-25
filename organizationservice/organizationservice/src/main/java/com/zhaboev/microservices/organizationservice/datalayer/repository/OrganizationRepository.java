package com.zhaboev.microservices.organizationservice.datalayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhaboev.microservices.organizationservice.datalayer.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {

}
