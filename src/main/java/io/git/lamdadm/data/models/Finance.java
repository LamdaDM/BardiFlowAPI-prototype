package io.git.lamdadm.data.models;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Finance implements Serializable {
    public Long id;
    public Double balance;
    public Double interest;
    public List<Transformer> transformers;

    public Finance(Double balance, Double interest) {
        this.balance = balance;
        this.interest = interest;
    }

    public Finance(Long id, Double balance, Double interest, List<Transformer> transformers) {
        this.id = id;
        this.balance = balance;
        this.interest = interest;
        this.transformers = Objects.requireNonNullElseGet(transformers, ArrayList::new);
    }

    public Finance(Long id, Double balance, Double interest) {
        this.id = id;
        this.balance = balance;
        this.interest = interest;
    }
}
