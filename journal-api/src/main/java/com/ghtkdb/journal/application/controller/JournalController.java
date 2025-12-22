package com.ghtkdb.journal.application.controller;

import com.ghtkdb.journal.application.entity.JournalEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public interface JournalController {

    @GetMapping("/getAll/{userName}")
    ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName);

    @GetMapping("/get/{myId}")
    ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId);

    @PostMapping("/create/{userName}")
    ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName);

    @DeleteMapping("delete/{userName}/{id}")
    ResponseEntity<?> deleteJournalEntryById(@PathVariable String id,@PathVariable String userName);

    @PutMapping("update/{userName}/{id}")
    ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable String id,@RequestBody JournalEntry myEntry,@PathVariable String userName);
}
