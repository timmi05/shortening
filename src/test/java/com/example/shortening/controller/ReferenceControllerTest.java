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
    private static final String ORIGINAL_REFERENCE = "original_reference";
    private static final String SHORT_REFERENCE = "short_reference";
    @InjectMocks
    private ReferenceController referenceController;
    @Mock
    private ReferenceService referenceService;

    @Test
    void shouldRedirectToOriginalReference() {
        when(referenceService.getOriginalReference(SHORT_REFERENCE)).thenReturn(ORIGINAL_REFERENCE);

        ResponseEntity<?> result = referenceController.redirectToOriginalReference(SHORT_REFERENCE);

        assertEquals(HttpStatus.MOVED_PERMANENTLY, result.getStatusCode());
        assertEquals(ORIGINAL_REFERENCE, result.getHeaders().getLocation().getPath());
    }

    @Test
    void shouldCreateShortReference() {
        Reference reference = new Reference();
        reference.setId(1);
        reference.setOriginalReference(ORIGINAL_REFERENCE);
        reference.setShortReference(SHORT_REFERENCE);
        when(referenceService.createShortReference(ORIGINAL_REFERENCE)).thenReturn(reference);

        Reference input = new Reference();
        input.setOriginalReference(ORIGINAL_REFERENCE);
        ResponseEntity<Reference> result = referenceController.createShortReference(input);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(reference, result.getBody());
    }
}
