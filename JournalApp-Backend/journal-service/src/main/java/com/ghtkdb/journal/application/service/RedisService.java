package com.ghtkdb.journal.application.service;

import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    public <T> T get(String key, Class<T> entityClass);
    void set(String key, Object o, Long ttl);
}
