package com.vdimri.generator;

import com.vdimri.generator.model.Addresses;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GeneratorController {

    private MeterRegistry meterRegistry;
    private GeneratorService generatorService = new GeneratorService();
    ;
    private RestTemplate restTemplate = new RestTemplate();

    private String generatorBaseUrl;

    public GeneratorController(@Value("${generator.base.url}") String generatorBaseUrl,
                               MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.generatorBaseUrl = generatorBaseUrl;
    }

    @Scheduled(fixedDelay = 10000)
    public String addPerson() {
        meterRegistry.counter("address.add").increment();
        log.debug("GeneratorBaseUrl :::::::::::::::::::" + generatorBaseUrl);
        HttpEntity<Addresses> request = new HttpEntity<>(generatorService.addressGenerator());
        log.debug("Request body::::::::::::::" + request.getBody());
        log.debug("Request header::::::::::::::" + request.getHeaders());
        ResponseEntity<String> entity = restTemplate.postForEntity
                (generatorBaseUrl + "/api/add", request, String.class);
        return entity.getBody();
    }
}
