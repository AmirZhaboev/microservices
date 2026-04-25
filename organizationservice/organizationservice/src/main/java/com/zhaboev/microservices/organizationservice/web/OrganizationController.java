package com.zhaboev.microservices.organizationservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaboev.microservices.organizationservice.datalayer.model.Organization;
import com.zhaboev.microservices.organizationservice.service.OrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/organization")
@Tag(name = "Organizations", description = "Операции с организациями")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Operation(summary = "Получить организацию по id")
    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> findOrganizationById(@PathVariable String organizationId) {
        Organization organization = organizationService.getOrganization(organizationId);
        return ResponseEntity.ok(organization);
    }

    @Operation(summary = "Создать организацию")
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization newOrganization) {
        Organization organization = organizationService.createOrganization(newOrganization);
        return ResponseEntity.status(201).body(organization);
    }

    @Operation(summary = "Обновить организацию по id")
    @PutMapping("/{organizationId}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable String organizationId,
            @RequestBody Organization newOrganization) {
        Organization organization = organizationService.updateOrganization(organizationId, newOrganization);
        return ResponseEntity.ok(organization);
    }

    @Operation(summary = "Удалить организацию по id")
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String organizationId) {
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.status(204).body(null);
    }

}
