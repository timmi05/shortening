package com.example.shortening.service;

import com.example.shortening.model.Reference;
import com.example.shortening.repository.ReferenceRepository;
import com.sun.jdi.InternalException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferenceServiceTest {
    private static final String ORIGINAL_URL = "original_url";
    private static final String SHORT_URL = "short_url";
    @InjectMocks
    private ReferenceService referenceService;
    @Mock
    private ReferenceRepository referenceRepository;

    @Test
    void shouldReturnOriginalUrl() {
        Reference reference = new Reference();
        reference.setOriginalUrl(ORIGINAL_URL);
        when(referenceRepository.findReferenceByShortUrl(SHORT_URL)).thenReturn(reference);

        String result = referenceService.getOriginalUrl(SHORT_URL);

        assertEquals(ORIGINAL_URL, result);
    }

    @Test
    void shouldThrowEntityNotFoundException_WhenShortUrlNotFound() {
        assertThrows(EntityNotFoundException.class, () -> referenceService.getOriginalUrl(SHORT_URL),
                "Url short_reference not found");
    }

    @Test
    void shouldCreateShortUrl() {
        ReflectionTestUtils.setField(referenceService, "min", 6);
        ReflectionTestUtils.setField(referenceService, "max", 8);

        when(referenceRepository.saveAndFlush(any())).then(returnsFirstArg());

        Reference result = referenceService.createShortUrl(ORIGINAL_URL);
        int shortUrlLength = result.getShortUrl().length();

        assertTrue(6 <= shortUrlLength && shortUrlLength <= 8);
    }

    @Test
    void shouldThrowInternalException_WhenCreateShortReference() {
        when(referenceRepository.findReferenceByShortUrl("")).thenReturn(new Reference());

        assertThrows(InternalException.class, () -> referenceService.createShortUrl(ORIGINAL_URL),
                "Internal server exception. Please, try one more time later");
    }
}
