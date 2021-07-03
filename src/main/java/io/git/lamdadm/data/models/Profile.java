package io.git.lamdadm.data.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Profile implements Serializable {
    public Long id;
    public String name;
    public String email;
    public String password;
    public Finance finance;

    public Profile(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Profile(Long id, String name, String email, String password, Finance finance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.finance = finance;
    }
}
