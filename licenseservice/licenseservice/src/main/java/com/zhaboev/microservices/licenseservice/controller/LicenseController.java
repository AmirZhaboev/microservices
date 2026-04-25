package com.zhaboev.microservices.licenseservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaboev.microservices.licenseservice.DTO.LicenseDTO;
import com.zhaboev.microservices.licenseservice.model.License;
import com.zhaboev.microservices.licenseservice.service.LicenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/organization/{organizationId}/license")
@Tag(name = "Licenses", description = "Операции с лицензиями")
public class LicenseController {

        private final LicenseService licenseService;

        @Operation(summary = "Получить лицензию по id")
        @GetMapping("/{licenseId}")
        public ResponseEntity<LicenseDTO> getLicense(
                        @PathVariable String organizationId,
                        @PathVariable String licenseId) {
                return ResponseEntity.ok(licenseService.getLicenseDto(licenseId, organizationId));
        }

        // @PatchMapping("/{licenseId}")
        // public ResponseEntity<String> updateLicense(@PathVariable("organizationId")
        // String organizationId,
        // @RequestBody License request) {
        // return ResponseEntity.ok(licenseService.updateLicense(request,
        // organizationId));
        // }
        @Operation(summary = "Добавить лицензию")
        @PostMapping
        public ResponseEntity<LicenseDTO> createLicense(
                        @PathVariable String organizationId,
                        @RequestBody License request) {
                request.setOrganizationId(organizationId);
                return ResponseEntity.status(HttpStatus.CREATED).body((licenseService.createLicenseDto(request)));
        }

        @Operation(summary = "Удалить лицензию по id")
        @DeleteMapping(value = "/{licenseId}")
        public ResponseEntity<Void> deleteLicense(
                        @PathVariable("organizationId") String organizationId,
                        @PathVariable("licenseId") String licenseId) {
                License license = licenseService.getLicense(licenseId, organizationId);
                licenseService.deleteLicense(license.getLicenseId());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

}
