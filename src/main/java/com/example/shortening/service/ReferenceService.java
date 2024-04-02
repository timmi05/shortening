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

    public String getOriginalUrl(String shortUrl) {
        Reference reference = referenceRepository
                .findReferenceByShortUrl(shortUrl);
        if (reference != null) {
            return reference.getOriginalUrl();
        }

        throw new EntityNotFoundException("Url " + shortUrl + " not found");
    }

    @Transactional
    public Reference createShortUrl(String originalUrl) {
        String shortUrl = generateShortUrl();

        Reference reference = new Reference();
        reference.setOriginalUrl(originalUrl);
        reference.setShortUrl(shortUrl);

        return referenceRepository.saveAndFlush(reference);
    }

    private String generateShortUrl() {
        String shortUrl = "";
        for (int index = 0; index < 10; index++) {
            shortUrl = RandomStringUtils.random(getRandomLengthOfShortUrl(), true, true);
            Reference reference = referenceRepository
                    .findReferenceByShortUrl(shortUrl);
            if (reference == null) {
                break;
            }
            if (index == 9) {
                throw new InternalException("Internal server exception. Please, try one more time later");
            }
        }
        return shortUrl;
    }

    public int getRandomLengthOfShortUrl() {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
