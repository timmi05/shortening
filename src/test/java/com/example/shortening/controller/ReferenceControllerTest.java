package com.example.shortening.controller;

import com.example.shortening.model.Reference;
import com.example.shortening.service.ReferenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferenceControllerTest {
    private static final String ORIGINAL_URL = "original_url";
    private static final String SHORT_URL = "short_url";
    @InjectMocks
    private ReferenceController referenceController;
    @Mock
    private ReferenceService referenceService;

    @Test
    void shouldRedirectToOriginalUrl() {
        when(referenceService.getOriginalUrl(SHORT_URL)).thenReturn(ORIGINAL_URL);

        ResponseEntity<?> result = referenceController.redirectToOriginalURL(SHORT_URL);

        assertEquals(HttpStatus.MOVED_PERMANENTLY, result.getStatusCode());
        assertEquals(ORIGINAL_URL, result.getHeaders().getLocation().getPath());
    }

    @Test
    void shouldCreateShortUrl() {
        Reference reference = new Reference();
        reference.setId(1);
        reference.setOriginalUrl(ORIGINAL_URL);
        reference.setShortUrl(SHORT_URL);
        when(referenceService.createShortUrl(ORIGINAL_URL)).thenReturn(reference);

        ResponseEntity<String> result = referenceController.createShortURL(ORIGINAL_URL);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(SHORT_URL, result.getBody());
    }
}
