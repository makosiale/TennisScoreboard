package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    private Player player1;

    @ManyToOne
    private Player player2;

    @ManyToOne
    private Player winner;

    @Transient
    private int score1;

    @Transient
    private int score2;

    @Transient
    private int scoreByGame1;

    @Transient
    private int scoreByGame2;

    @Transient
    private int scoreBySet1;

    @Transient
    private int scoreBySet2;
}
