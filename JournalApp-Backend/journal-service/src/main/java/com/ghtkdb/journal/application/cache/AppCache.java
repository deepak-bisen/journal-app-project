package com.ghtkdb.journal.application.cache;

import com.ghtkdb.journal.application.entity.ConfigJournalApp;
import com.ghtkdb.journal.application.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
        Weather_API;
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> appCache;

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalApp> all = configJournalAppRepository.findAll();
        for (ConfigJournalApp configJournalApp : all) {
            appCache.put(configJournalApp.getKey(), configJournalApp.getValue());
        }
    }

}
