package com.example.shortening.repository;

import com.example.shortening.model.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {
    Reference findReferenceByShortReference(String shortReference);
}
