package com.zhaboev.microservices.organizationservice.datalayer.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
public class Organization {
    @Id
    @Column(name = "organization_id", nullable = false)
    private String organizationId = UUID.randomUUID().toString();;
    @Column(name = "organization_name", nullable = false)
    private String organizationName;
    @Column(name = "organization_phone", nullable = false)
    private String organizationPhone;
    @Column(name = "organization_email", nullable = false)
    private String organizationEmail;
    @Column(name = "organization_address")
    private String organizationAddress;
    @Column(name = "organization_city")
    private String organizationCity;
}
