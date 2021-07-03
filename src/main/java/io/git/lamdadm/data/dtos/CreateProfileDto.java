package io.git.lamdadm.data.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@Introspected
@AllArgsConstructor
public class CreateProfileDto implements Serializable {
    public String name;
    public String email;
    public String password;
}