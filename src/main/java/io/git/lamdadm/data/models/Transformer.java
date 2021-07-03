package io.git.lamdadm.data.models;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Transformer implements Serializable {
    public Long id;
    public String name;
    public String description;
    public Double interest;
    public Double amount;
    public Double payment;
    public Integer cycle;
    public LocalDate referenceDate;

    public Transformer(Long id, String name, String description, Double interest,
                       Double amount, Double payment, Integer cycle, LocalDate referenceDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.interest = interest;
        this.amount = amount;
        this.payment = payment;
        this.cycle = cycle;
        this.referenceDate = referenceDate;
    }
}
