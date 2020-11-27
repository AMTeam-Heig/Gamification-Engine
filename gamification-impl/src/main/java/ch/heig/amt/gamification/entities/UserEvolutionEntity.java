package ch.heig.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class UserEvolutionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private UserEntity user;

    @OneToMany
    private List<PointTimeEntity> pointTime;
}
