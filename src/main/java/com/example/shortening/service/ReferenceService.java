package com.example.shortening.service;

import com.example.shortening.model.Reference;
import com.example.shortening.repository.ReferenceRepository;
import com.sun.jdi.InternalException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReferenceService {
    private final ReferenceRepository referenceRepository;

    @Value("${reference.size.min}")
    private int min;
    @Value("${reference.size.max}")
    private int max;

    public String getOriginalReference(String shortReference) {
        Reference reference = referenceRepository
                .findReferenceByShortReference(shortReference);
        if (reference != null) {
            return reference.getOriginalReference();
        }

        throw new EntityNotFoundException("Url " + shortReference + " not found");
    }

    @Transactional
    public Reference createShortReference(String originalReference) {
        String shortReference = getShortReference();

        Reference reference = new Reference();
        reference.setOriginalReference(originalReference);
        reference.setShortReference(shortReference);

        return referenceRepository.saveAndFlush(reference);
    }

    private String getShortReference() {
        String shortReference = "";
        for (int index = 0; index < 10; index++) {
            shortReference = RandomStringUtils.random(getRandomLengthOfShortReference(), true, true);
            Reference reference = referenceRepository
                    .findReferenceByShortReference(shortReference);
            if (reference == null) {
                break;
            }
            if (index == 9) {
                throw new InternalException("Internal server exception, Please, try one more time later");
            }
        }
        return shortReference;
    }

    public int getRandomLengthOfShortReference() {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
