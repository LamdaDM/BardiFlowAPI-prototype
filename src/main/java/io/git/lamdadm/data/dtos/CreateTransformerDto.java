package io.git.lamdadm.data.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.time.LocalDate;

@Data
@Introspected
public class CreateTransformerDto {
    public String name;
    public String description;
    public Double interest;
    public Double amount;
    public Double payment;
    public int cycle;
    public LocalDate reference_date;
}