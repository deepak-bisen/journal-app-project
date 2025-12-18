package com.ghtkdb.journal.application.controller.impl;

import com.ghtkdb.journal.application.controller.JournalController;
import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.service.JournalEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<JournalEntry> getAllEntry(){
        log.info("getting entries {}",journalEntryService.getAllEntry() );
        return journalEntryService.getAllEntry();
    }

    @Override
    public Optional<JournalEntry> getJournalEntryById(@PathVariable String myId){
        return journalEntryService.getJournalEntryById(myId);
    }

    @Override
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry){

        return journalEntryService.createEntry(myEntry);
    }

    @Override
    public boolean deleteJournalEntryById(@PathVariable String id){
        journalEntryService.deleteJournalEntryById(id);
        return true;
    }

    @Override
    public JournalEntry updateJournalEntryById(@PathVariable String id,@RequestBody JournalEntry myEntry){
        return journalEntryService.updateJournalEntryById(id,myEntry);
    }

}
