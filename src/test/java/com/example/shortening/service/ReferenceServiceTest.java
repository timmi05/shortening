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
    private static final String ORIGINAL_REFERENCE = "original_reference";
    private static final String SHORT_REFERENCE = "short_reference";
    @InjectMocks
    private ReferenceService referenceService;
    @Mock
    private ReferenceRepository referenceRepository;

    @Test
    void shouldReturnOriginalReference() {
        Reference reference = new Reference();
        reference.setOriginalReference(ORIGINAL_REFERENCE);
        when(referenceRepository.findReferenceByShortReference(SHORT_REFERENCE)).thenReturn(reference);

        String result = referenceService.getOriginalReference(SHORT_REFERENCE);

        assertEquals(ORIGINAL_REFERENCE, result);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGetOriginalReference() {
        assertThrows(EntityNotFoundException.class, () -> referenceService.getOriginalReference(SHORT_REFERENCE),
                "Url short_reference not found");
    }

    @Test
    void shouldCreateShortReference() {
        ReflectionTestUtils.setField(referenceService, "min", 6);
        ReflectionTestUtils.setField(referenceService, "max", 8);

        when(referenceRepository.saveAndFlush(any())).then(returnsFirstArg());

        Reference result = referenceService.createShortReference(ORIGINAL_REFERENCE);
        int shortReferenceLength = result.getShortReference().length();

        assertTrue(6 <= shortReferenceLength && shortReferenceLength <= 8);
    }

    @Test
    void shouldThrowInternalExceptionWhenCreateShortReference() {
        when(referenceRepository.findReferenceByShortReference("")).thenReturn(new Reference());

        assertThrows(InternalException.class, () -> referenceService.createShortReference(ORIGINAL_REFERENCE),
                "Internal server exception. Please, try one more time later");
    }
}
