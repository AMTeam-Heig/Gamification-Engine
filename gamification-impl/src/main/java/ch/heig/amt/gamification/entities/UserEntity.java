package ch.heig.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int role;

    @Column
    private String username;

    @Column
    private String reputation;

    @Column
    private int points;

    @Column
    private LocalDate birthdate;

    @ManyToOne
    private ApplicationEntity applicationEntity;

}
