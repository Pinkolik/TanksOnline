package ru.urfu.Server.Models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "rounds")
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime roundStart;
    private LocalDateTime roundEnd;
    private Integer firedShotsCount;
    private Integer madeMovesCount;
    @ManyToMany(mappedBy = "playedRounds")
    private Set<UserStatistics> allPlayers;

    public Set<UserStatistics> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(Set<UserStatistics> allPlayers) {
        this.allPlayers = allPlayers;
    }

    public UserStatistics getWinner() {
        return winner;
    }

    public void setWinner(UserStatistics winner) {
        this.winner = winner;
    }

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private UserStatistics winner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRoundStart() {
        return roundStart;
    }

    public void setRoundStart(LocalDateTime roundStart) {
        this.roundStart = roundStart;
    }

    public LocalDateTime getRoundEnd() {
        return roundEnd;
    }

    public void setRoundEnd(LocalDateTime roundEnd) {
        this.roundEnd = roundEnd;
    }

    public Integer getFiredShotsCount() {
        return firedShotsCount;
    }

    public void setFiredShotsCount(Integer firedShotsCount) {
        this.firedShotsCount = firedShotsCount;
    }

    public Integer getMadeMovesCount() {
        return madeMovesCount;
    }

    public void setMadeMovesCount(Integer madeMovesCount) {
        this.madeMovesCount = madeMovesCount;
    }
}
