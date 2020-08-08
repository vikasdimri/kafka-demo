package com.vdimri.producer.controller;

import com.vdimri.producer.model.Address;
import com.vdimri.producer.services.AddressBookService;
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

    private AddressBookService service;

    public AddressBookController(MeterRegistry meterRegistry, AddressBookService service) {
        this.meterRegistry = meterRegistry;
        this.service = service;
    }

    @ApiOperation(value = "Endpoint to add address in the address book")
    @PostMapping(path= "/add", consumes = "application/json")
    public void addInAddressBook(@RequestBody String address) {
        meterRegistry.counter("address.add.request").increment();
        service.publish(address);
        log.debug("Add address: "+address.toString());
    }
}