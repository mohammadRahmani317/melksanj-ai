package com.realstate.divar.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Neighborhood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private City city;
}
