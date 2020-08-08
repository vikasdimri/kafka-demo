package com.vdimri.generator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdimri.generator.model.Addresses;
import com.vdimri.generator.model.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class AddressGeneratorService {

    private Root root;
    private int offset = 0;
    private int size;
    private int position = 0;

    public Addresses addressGenerator() throws IOException {
        Addresses address = null;
        if (root == null) {
            root = readAddressesJson();
            log.debug("Root :: ",root.toString());
        }
        address = getAddress();
        return address;
    }

    private Addresses getAddress() {
        Addresses address = null;
        List<Addresses> addresses = root.getAddresses();
        if (!CollectionUtils.isEmpty(addresses)) {
            size = root.getAddresses().size();
            address = addresses.get(offset);
            offset++;
        }
        if (size == offset) {
            addressFactory();
        }
        return address;
    }

    private void addressFactory() {
        offset = 0;
        position++;
        Random random = new Random();
        for (Addresses addressToChange : root.getAddresses()) {
            addressToChange.setPostalCode(String.valueOf(random.nextInt(99999)));
        }
    }

    private Root readAddressesJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = ResourceUtils.getFile("classpath:addresses.json");
        InputStream inputStream = new FileInputStream(file);
        //Resource resource = new ClassPathResource("classpath:addresses.json");
        //InputStream inputStream = resource.getInputStream();
        return objectMapper.readValue(inputStream, Root.class);
    }
}
