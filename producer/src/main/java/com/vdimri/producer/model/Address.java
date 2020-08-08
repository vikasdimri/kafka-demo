package com.vdimri.producer.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Address implements Serializable {
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
}
