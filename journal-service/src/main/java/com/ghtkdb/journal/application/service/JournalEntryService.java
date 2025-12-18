package com.ghtkdb.journal.application.service;

import com.ghtkdb.journal.application.entity.JournalEntry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface JournalEntryService {

    public JournalEntry createEntry(JournalEntry entry);
    public List<JournalEntry> getAllEntry();
    public Optional<JournalEntry> getJournalEntryById(String uuid);
    public JournalEntry updateJournalEntryById(String id, JournalEntry myEntry);
    public void deleteJournalEntryById(String uuid);
}
