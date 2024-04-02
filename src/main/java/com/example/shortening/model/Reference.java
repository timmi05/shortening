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
    @Column(name = "short_url", unique=true)
    private String shortUrl;
    @Column(name = "original_url")
    private String originalUrl;
}
