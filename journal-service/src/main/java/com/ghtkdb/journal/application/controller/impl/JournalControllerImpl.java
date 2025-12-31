package com.ghtkdb.journal.application.controller.impl;

import com.ghtkdb.journal.application.controller.JournalController;
import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.service.JournalEntryService;
import com.ghtkdb.journal.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@Slf4j
public class JournalControllerImpl implements JournalController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @Override
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        try {
            log.info("inside @class JournalControllerImpl in @method getAllJournalEntriesOfUser");
            log.info("getting entries of user.");
            return new ResponseEntity<>(journalEntryService.getAllUserEntriesOfUser(), HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("some error occurred when getting entries!");
            log.info("Exception : {}", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @Override
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String uuid) {
        log.info("inside @class JournalControllerImpl in @method getAllJournalEntryById");

        log.info("getting entries by id....");
        try {
            Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(uuid);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("some error occurred : {}", e);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<JournalEntry> createEntry(JournalEntry myEntry) {
        log.info("inside @class JournalControllerImpl in @method createEntry");

        try {
            log.info("saving entry of user...");
            journalEntryService.createEntry(myEntry);
            log.info("Entry Saved : {}", myEntry);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Can't create entry : {}", myEntry);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<?> deleteJournalEntryById(String uuid) {
        log.info("inside @class JournalControllerImpl in @method deleteJournalEntryById");

        try {
            journalEntryService.deleteJournalEntryById(uuid);
            log.info("Entry Deleted Using Given Id : {}", uuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Can't delete! by given Id : {}", uuid);
            log.error("error occurred : {}", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<JournalEntry> updateJournalEntryById(String uuid, JournalEntry myEntry) {
        log.info("inside @class JournalControllerImpl in @method updateJournalEntryById");

        try {
            log.info("Updating journal entry for id: {}", uuid);
            JournalEntry updatedEntry = journalEntryService.updateJournalEntryById(uuid, myEntry);
            log.info("updated entry : {}", myEntry);
            return new ResponseEntity<>(updatedEntry, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Update failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}