package com.springboot.redis.tinyurl.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TinyUrlError {

    private final String message;
}
