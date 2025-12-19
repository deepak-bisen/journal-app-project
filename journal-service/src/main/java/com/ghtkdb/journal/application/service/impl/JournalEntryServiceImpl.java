package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.repository.JournalEntryRepository;
import com.ghtkdb.journal.application.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryServiceImpl implements JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Override
    public JournalEntry createEntry(JournalEntry entry) {
        entry.setDate(LocalDateTime.now());
        journalEntryRepository.save(entry);
        return entry;
    }

    @Override
    public List<JournalEntry> getAllEntry() {
        return journalEntryRepository.findAll();
    }

    @Override
    public Optional<JournalEntry> getJournalEntryById(String uuid) {
        return journalEntryRepository.findById(uuid);
    }

    @Override
    public JournalEntry updateJournalEntryById(String uuid, JournalEntry newEntry) {

        JournalEntry oldEntry = journalEntryRepository.findById(uuid).orElseThrow(null);

        if (oldEntry != null ){
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
