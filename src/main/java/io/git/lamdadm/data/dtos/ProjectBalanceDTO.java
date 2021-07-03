package io.git.lamdadm.data.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.time.LocalDate;

@Data
@Introspected
public class ProjectBalanceDTO {
    public LocalDate target;
}
