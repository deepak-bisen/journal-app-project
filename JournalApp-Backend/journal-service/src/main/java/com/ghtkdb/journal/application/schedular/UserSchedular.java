package com.ghtkdb.journal.application.schedular;

import com.ghtkdb.journal.application.cache.AppCache;
import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.enums.Sentiment;
import com.ghtkdb.journal.application.repository.UserRepositoryImpl;
import com.ghtkdb.journal.application.service.EmailService;
import com.ghtkdb.journal.application.service.impl.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserSchedular {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppCache appCache;

    // 0 * * ? * *  every minute
    @Scheduled(cron = "0 0 9 * * SUN")  //every sunday 9 am
    public void fetchUsersAndSendMail() {
        List<User> users = userRepository.getUserForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
                Sentiment mostFrequentSentiment = null;
                int maxCount = 0;
                for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        mostFrequentSentiment = entry.getKey();
                    }
                }
                if (mostFrequentSentiment != null) {
                    emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSentiment.toString());

                }
            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")  //every 10 minutes
    public void clearAppCache() {
        appCache.init();
    }
}
