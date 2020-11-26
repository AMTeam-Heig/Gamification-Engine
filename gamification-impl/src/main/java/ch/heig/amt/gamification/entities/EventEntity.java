package ch.heig.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class EventEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private int userId;

    @Column
    private int points;

    @ManyToOne
    private ApplicationEntity applicationEntity;
}
