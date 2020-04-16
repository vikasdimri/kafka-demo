package com.vdimri.producer;

import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class AddressBookController {

    private MeterRegistry meterRegistry;

    public AddressBookController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @ApiOperation(value = "Endpoint to add address in the address book")
    @PostMapping(path= "/add", consumes = "application/json", produces = "application/json")
    public void addInAddressBook(@RequestBody String request) {
        meterRegistry.counter("address.add.request").increment();
        log.debug("Add address: "+request.toString());
    }
}