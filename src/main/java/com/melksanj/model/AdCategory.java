package com.melksanj.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AdCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String code;

    @Column(length = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private AdGroup group;

    public AdCategory(String code) {
        this.code = code;
    }
}

