package io.avalia.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class BadgeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String level;
    private String description;

    @Column(columnDefinition = "DATE")
    private LocalDate obtainedOnDate;
}
