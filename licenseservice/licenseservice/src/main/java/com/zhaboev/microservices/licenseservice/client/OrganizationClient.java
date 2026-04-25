package com.zhaboev.microservices.licenseservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.zhaboev.microservices.licenseservice.exception.OrganizationNotFoundClientException;
import com.zhaboev.microservices.licenseservice.model.Organization;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class OrganizationClient {
    private final RestClient restClient;
    private final String organizationServiceBaseUrl;

    public OrganizationClient(
            RestClient restClient,
            @Value("${services.organization.url}") String organizationServiceBaseUrl) {
        this.restClient = restClient;
        this.organizationServiceBaseUrl = organizationServiceBaseUrl;
    }

    @CircuitBreaker(name = "organizationService", fallbackMethod = "fallbackOrg")
    @Bulkhead(name = "organizationService", fallbackMethod = "fallbackOrg")
    @RateLimiter(name = "organizationService", fallbackMethod = "fallbackOrg")
    @Retry(name = "organizationService", fallbackMethod = "fallbackOrg")
    public Organization getOrganization(String organizationId) {
        System.out.println("CALL organization-service: " + organizationId);
        try {
            Organization org = restClient.get()
                    .uri(organizationServiceBaseUrl + "/v1/organization/{organizationId}", organizationId)
                    .retrieve()
                    .body(Organization.class);
            System.out.println("CALL organization-service DONE: " + organizationId);
            return org;
        } catch (HttpClientErrorException.NotFound e) {
            throw new OrganizationNotFoundClientException(
                    "Organization not found: " + organizationId);
        }
    }

    public Organization fallbackOrg(String organizationId, Throwable t) {
        System.out.println("FALLBACK: " + t.getClass().getSimpleName());
        Organization organization = new Organization();
        organization.setOrganizationId(organizationId);
        organization.setOrganizationName("Организация временно недоступна");
        return organization;
    }
}
