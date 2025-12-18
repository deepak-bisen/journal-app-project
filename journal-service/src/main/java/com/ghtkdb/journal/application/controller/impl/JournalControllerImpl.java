package com.ghtkdb.journal.application.controller.impl;

import com.ghtkdb.journal.application.controller.JournalController;
import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.service.JournalEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class JournalControllerImpl implements JournalController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Override
    public ResponseEntity<List<JournalEntry>> getAllEntry() {
        try {
            List<JournalEntry> allEntries = journalEntryService.getAllEntry();
            if (allEntries != null && !allEntries.isEmpty()) {
                log.info("getting entries {}", journalEntryService.getAllEntry());
                return new ResponseEntity<>(allEntries, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("some error occurred when getting entries!");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Override
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(myId);
        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            log.info("saving entry...");
            journalEntryService.createEntry(myEntry);
            log.info("Entry Saved : {}", myEntry);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Can't create entry : {}", myEntry);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String id) {
        try {
            journalEntryService.deleteJournalEntryById(id);
            log.info("Entry Deleted Using Given Id : {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Can't delete! by given Id : {}", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable String id, @RequestBody JournalEntry myEntry) {
        try {
            log.info("updating entry for given id : {}", id);
            log.info("updated entry : {}", myEntry);
            return new ResponseEntity<>(journalEntryService.updateJournalEntryById(id, myEntry), HttpStatus.OK);
        } catch (Exception e) {
            log.error("entry not found! for given id : {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}