package com.ghtkdb.journal.application.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime date;
}
