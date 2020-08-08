package com.vdimri.generator.scheduler;

import com.vdimri.generator.model.Addresses;
import com.vdimri.generator.services.AddressGeneratorService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Component
public class AddressScheduler {

    private MeterRegistry meterRegistry;
    private AddressGeneratorService service = new AddressGeneratorService();
    private RestTemplate restTemplate;
    private String baseUrl;

    public AddressScheduler(MeterRegistry meterRegistry,
                            @Value("${generator.base.url}") String baseUrl) {
        this.meterRegistry = meterRegistry;
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(fixedDelay = 10000)
    public String publishAddressToKafka() {
        log.debug("GeneratorBaseUrl :: {}", baseUrl);
        HttpEntity<Addresses> request = null;
        String response = "";
        try {
            request = new HttpEntity<>(service.addressGenerator());
            meterRegistry.counter("address.generate").increment();
            log.debug("Request body :: {}", request.getBody());
            log.debug("Request header:: {}", request.getHeaders());
            log.debug("url:: {}", baseUrl + "/api/add");
            ResponseEntity<String> entity = restTemplate.postForEntity
                    (baseUrl + "/api/add", request, String.class);
            response = entity.getBody();
        } catch (IOException ioException) {
            log.error("Error message :: {}", ioException.getMessage());
        }
        log.debug("Response :: {}", response);
        return response;
    }
}
