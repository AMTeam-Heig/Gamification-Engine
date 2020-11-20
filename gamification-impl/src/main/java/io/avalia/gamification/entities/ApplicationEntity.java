package io.avalia.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class ApplicationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String apiKey;

    @Column(unique = true)
    private String name;

    @OneToMany
    private List<BadgeEntity> badges;

    static public String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
