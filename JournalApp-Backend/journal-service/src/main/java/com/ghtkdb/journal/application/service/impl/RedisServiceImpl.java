package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisServiceImpl( RedisTemplate<String, Object> redisTemplate){
    this.redisTemplate = redisTemplate;
    }
    
    @Override
    public <T> T get(String key, Class<T> entityClass) {
        try {
            log.info("Inside @Class RedisServiceImpl inside @Method get");
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);
        } catch (JsonProcessingException e) {
            log.error("Jackson Exception during Redis GET: ", e);
            return null;
        }
    }

    @Override
    public void set(String key, Object o, Long ttl) {
        try {
            log.info("Inside @Class RedisServiceImpl inside @Method set");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("Jackson Exception during Redis SET: ", e);
         }
    }
}
