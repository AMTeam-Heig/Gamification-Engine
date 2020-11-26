package ch.heig.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class RuleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String definition;

    @Column
    private String reputation;

    @Column
    private int points;

    @Column
    private String eventName;

    @Column
    private String badgeName;

    @ManyToOne
    private ApplicationEntity applicationEntity;

}
