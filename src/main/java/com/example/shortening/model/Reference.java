package com.example.shortening.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reference")
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "short_reference", unique=true)
    private String shortReference;
    @Column(name = "original_reference")
    private String originalReference;
}
