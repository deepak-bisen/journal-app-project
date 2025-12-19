package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.repository.JournalEntryRepository;
import com.ghtkdb.journal.application.repository.UserRepository;
import com.ghtkdb.journal.application.service.JournalEntryService;
import com.ghtkdb.journal.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JournalEntryServiceImpl implements JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public JournalEntry createEntry(JournalEntry entry, String userName) {
        User user = userRepository.findByUserName(userName);
        entry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(entry);
        user.getJournalEntries().add(saved);
        userService.createUser(user);
        return entry;
    }

    @Override
    public List<JournalEntry> getAllUserEntriesOfUser(String userName) {
        User user = userRepository.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if (allEntries != null && !allEntries.isEmpty()) {
            log.info("getting entries oif username : {}", allEntries);
            return allEntries;
        }
        return null;
    }

    @Override
    public Optional<JournalEntry> getJournalEntryById(String uuid) {
        return journalEntryRepository.findById(uuid);
    }

    @Override
    public JournalEntry updateJournalEntryById(String uuid, JournalEntry newEntry) {

        JournalEntry oldEntry = journalEntryRepository.findById(uuid).orElseThrow(null);

        if (oldEntry != null) {
            oldEntry.setTitle(newEntry.getTitle());
            oldEntry.setContent(newEntry.getTitle());
            oldEntry.setEntryId(newEntry.getEntryId());
            oldEntry.setDate(LocalDateTime.now());

            journalEntryRepository.save(oldEntry);
        }

        return oldEntry;
    }

    @Override
    public void deleteJournalEntryById(String uuid) {
        journalEntryRepository.deleteById(uuid);
    }
}
