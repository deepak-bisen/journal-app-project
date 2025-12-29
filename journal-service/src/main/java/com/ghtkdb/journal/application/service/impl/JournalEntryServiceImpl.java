package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.repository.JournalEntryRepository;
import com.ghtkdb.journal.application.repository.UserRepository;
import com.ghtkdb.journal.application.service.JournalEntryService;
import com.ghtkdb.journal.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public JournalEntry createEntry(JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        entry.setUser(user);
        JournalEntry saved = journalEntryRepository.save(entry);
        user.getJournalEntries().add(saved);
        return entry;
    }


    @Override
    public List<JournalEntry> getAllUserEntriesOfUser() {
        log.info("inside @class JournalEntryServiceImpl inside @method getAllUserEntriesOfUser ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if (allEntries != null && !allEntries.isEmpty()) {
            log.info("Getting Entries Of UserName : {}", allEntries);
            return allEntries;
        }
        return null;
    }

    @Override
    public Optional<JournalEntry> getJournalEntryById(String uuid) {
        log.info("inside getJournalEntryById for uuid: {}", uuid);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUserName(username);

        // 1. Null check for user
        if (user == null) {
            log.warn("User {} not found", username);
            return Optional.empty();
        }

        // 2. Check if the entry belongs to this user
        boolean ownsEntry = user.getJournalEntries().stream()
                .anyMatch(x -> x.getUuid().equals(uuid));

        if (ownsEntry) {
            return journalEntryRepository.findById(uuid);
        }

        log.warn("User {} does not own entry {}", username, uuid);
        return Optional.empty(); // Never return null for an Optional
    }

    @Override
    public void deleteJournalEntryById(String uuid) {
        log.info("inside @class JournalEntryServiceImpl inside @method deleteJournalEntryById ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        boolean removed = user.getJournalEntries().removeIf(x -> x.getUuid().equals(uuid));
        if (removed) {
            //  Save the user. Because of orphanRemoval=true, JPA will delete the entry from the DB.
            userRepository.save(user);
        } else {
            log.error("Entry with uuid {} not found for user {}", uuid, userName);
            throw new RuntimeException("Entry not found in user's collection");
        }
    }

    @Override
    @Transactional
    public JournalEntry updateJournalEntryById(String uuid, JournalEntry newEntry) {

        log.info("inside @class JournalEntryServiceImpl inside @method updateJournalEntryById ");
        // Get the username from the Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Verify the user exists
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Fetch the existing entry and verify ownership
        JournalEntry oldEntry = journalEntryRepository.findById(uuid)
                .filter(entry -> entry.getUser().getUserName().equals(username))
                .orElseThrow(() -> new RuntimeException("Entry not found or access denied"));

        // Update fields
        if (newEntry.getTitle() != null) oldEntry.setTitle(newEntry.getTitle());
        if (newEntry.getContent() != null) oldEntry.setContent(newEntry.getContent());

        return journalEntryRepository.save(oldEntry);
    }
}
