package com.ghtkdb.journal.application.controller.impl;

import com.ghtkdb.journal.application.controller.JournalController;
import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.service.JournalEntryService;
import com.ghtkdb.journal.application.service.UserService;
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

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> getAllJournalEntriesOfUser(String userName) {
        try {
            log.info("getting entries of username : {}", userName);
            return new ResponseEntity<>(journalEntryService.getAllUserEntriesOfUser(userName),HttpStatus.FOUND);
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
    public ResponseEntity<JournalEntry> createEntry(JournalEntry myEntry,String userName) {
        try {

            log.info("saving entry of user : {}", userName);
            journalEntryService.createEntry(myEntry, userName);
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