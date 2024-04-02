package com.example.shortening.controller;

import com.example.shortening.model.Reference;
import com.example.shortening.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ReferenceController {
    private final ReferenceService referenceService;

    @GetMapping(value = {"/{shortUrl}"})
    public ResponseEntity<?> redirectToOriginalReference(@PathVariable String shortUrl) {
        String originalReference = referenceService.getOriginalReference(shortUrl);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, originalReference).build();
    }

    @PostMapping(value = {"/shorten"})
    public ResponseEntity<Reference> createShortReference(@RequestBody String reference) {
        Reference newReference = referenceService.createShortReference(reference);
        log.info("Called method create short reference for full reference: {}", reference);
        return new ResponseEntity<>(newReference, HttpStatus.CREATED);
    }
}
