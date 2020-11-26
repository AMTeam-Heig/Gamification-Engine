package ch.heig.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class BadgeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String level;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private LocalDate obtainedOnDate;

    @ManyToOne
    private ApplicationEntity applicationEntity;
}
