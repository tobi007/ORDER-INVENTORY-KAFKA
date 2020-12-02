package com.tobi.order.processor.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

    private String name;
    private String email;
    private String phoneNumber;

    @Override
    public String toString() {
        return "Customer [name=" + name + ", email="
                + email + ", phoneNumber=" + phoneNumber + "]";
    }
}
