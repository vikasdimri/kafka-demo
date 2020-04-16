package com.vdimri.generator.model;

import lombok.Data;

@Data
public class Addresses {
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private Coordinates coordinates;
}
