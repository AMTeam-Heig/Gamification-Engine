package ch.heig.amt.gamification.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
