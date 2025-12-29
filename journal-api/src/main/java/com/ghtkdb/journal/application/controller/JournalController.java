package com.ghtkdb.journal.application.controller;

import com.ghtkdb.journal.application.entity.JournalEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/journal")
public interface JournalController {

    @GetMapping("/getAll")
    ResponseEntity<?> getAllJournalEntriesOfUser();

    @GetMapping("/get/{uuid}")
    ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String uuid);

    @PostMapping("/create")
    ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry);

    @DeleteMapping("delete/{uuid}")
    ResponseEntity<?> deleteJournalEntryById(@PathVariable String uuid);

    @PutMapping("update/{uuid}")
    ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable String uuid,@RequestBody JournalEntry myEntry);
}