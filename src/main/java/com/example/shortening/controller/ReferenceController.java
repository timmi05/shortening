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

    @GetMapping(value = {"/{url}"})
    public ResponseEntity<?> redirectToOriginalReference(@PathVariable String url) {
        String originalReference = referenceService.getOriginalReference(url);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, originalReference).build();
    }

    @PostMapping(value = {"/"})
    public ResponseEntity<Reference> createShortReference(@RequestBody Reference reference) {
        Reference newReference = referenceService.createShortReference(reference.getOriginalReference());
        log.info("Called method create short reference for full reference: {}", reference.getOriginalReference());
        return new ResponseEntity<>(newReference, HttpStatus.CREATED);
    }
}
