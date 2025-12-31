package com.ghtkdb.journal.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "CONFIG_JOURNAL_APP")
public class ConfigJournalApp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "CONFIG_KEY", nullable = false)
    private  String key;

    @Column(name = "CONFIG_VALUE", nullable = false)
    private  String value;
}
