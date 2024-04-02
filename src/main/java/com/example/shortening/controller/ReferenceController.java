package com.example.shortening.controller;

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
    public ResponseEntity<?> redirectToOriginalURL(@PathVariable String shortUrl) {
        String originalUrl = referenceService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, originalUrl).build();
    }

    @PostMapping(value = {"/shorten"})
    public ResponseEntity<String> createShortURL(@RequestBody String originalUrl) {
        String shortUrl = referenceService.createShortUrl(originalUrl).getShortUrl();
        log.info("Created short URL: {} for original URL: {}", shortUrl, originalUrl);
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
    }
}
