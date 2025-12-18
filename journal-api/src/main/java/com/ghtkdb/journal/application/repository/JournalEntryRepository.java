package com.ghtkdb.journal.application.repository;

import com.ghtkdb.journal.application.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, String> {
}
