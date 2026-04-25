package com.zhaboev.microservices.licenseservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigTestController {
    @Value("${example.property:default example}")
    private String exampleProperty;

    @Value("${example.global-property:default global example}")
    private String globalProperty;

    @Value("${custom.message:default custom meassage}")
    private String customMessage;

    @GetMapping("/config-test")
    public String configTest() {
        return "example.property=" + exampleProperty
                + ", global=" + globalProperty
                + ", custom.message=" + customMessage;
    }
}
