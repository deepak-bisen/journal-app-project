package com.ghtkdb.journal.application.controller;

import com.ghtkdb.journal.application.entity.JournalEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public interface JournalController {

    @GetMapping("/get")
    List<JournalEntry> getAllEntry();

    @GetMapping("/get/{myId}")
    Optional<JournalEntry> getJournalEntryById(@PathVariable String myId);

    @PostMapping("/create")
    JournalEntry createEntry(@RequestBody JournalEntry myEntry);

    @DeleteMapping("delete/{id}")
    boolean deleteJournalEntryById(@PathVariable String id);

    @PutMapping("update/{id}")
    JournalEntry updateJournalEntryById(@PathVariable String id,@RequestBody JournalEntry myEntry);
}
