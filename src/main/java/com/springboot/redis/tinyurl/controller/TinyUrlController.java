package com.springboot.redis.tinyurl.controller;

import com.google.zxing.WriterException;
import com.springboot.redis.tinyurl.exception.TinyUrlError;
import com.springboot.redis.tinyurl.model.GenerateQRCode;
import com.springboot.redis.tinyurl.model.UrlDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/rest/url")
public class TinyUrlController {

    @Autowired
    private RedisTemplate<String, UrlDto> redisTemplate;

    @Value("${redis.ttl}")
    private long ttl;

	@PostMapping
    public Object create(@RequestBody final String url) throws IOException, WriterException {
        final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (!urlValidator.isValid(url)) {
            return ResponseEntity.badRequest().body(new TinyUrlError("Invalid URL."));
        }

        final UrlDto urlDto = UrlDto.create(url);
        GenerateQRCode.generateQRcode(url);
        log.info("URL id generated = {}", urlDto.getId());
        redisTemplate.opsForValue().set(urlDto.getId(), urlDto, ttl, TimeUnit.SECONDS);
//        return ResponseEntity.noContent().header("id", urlDto.getId()).build();
        return urlDto.getId();

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUrl(@PathVariable final String id) throws URISyntaxException {
        final UrlDto urlDto = redisTemplate.opsForValue().get(id);
        if (Objects.isNull(urlDto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TinyUrlError("No such key exists."));
        } else {
            log.info("URL retrieved = {}", urlDto.getUrl());
        }

        URI url = new URI(urlDto.getUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(url);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @RequestMapping("home")
    public void homePage() {
        System.out.println("Home Page");
    }
}
