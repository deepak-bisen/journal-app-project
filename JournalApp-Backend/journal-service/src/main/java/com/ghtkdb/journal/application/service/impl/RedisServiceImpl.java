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

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public <T> T get(String key, Class<T> entityClass) {
        try {
            log.info("Insidde @Class RedisServiceImpl inside @Method get");
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);
        } catch (JacksonException e) {
            log.error("Exception : ",   e);
            return null;
        }
    }

    @Override
    public void set(String key, Object o, Long ttl) {
        try {
            log.info("Insidde @Class RedisServiceImpl inside @Method set");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (JacksonException e) {
            log.error("Exception : ",   e);
         }
    }
}
