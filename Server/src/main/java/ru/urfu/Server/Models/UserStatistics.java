package ru.urfu.Server.Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users_statistics")
public class UserStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer killsCount = 0;
    private Integer deathsCount = 0;
    private Integer firedShotsCount = 0;
    private Integer madeMovesCount = 0;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(mappedBy = "allPlayers")
    private Set<Round> playedRounds = new HashSet<>();
    @OneToMany(mappedBy = "winner")
    private Set<Round> wonRounds = new HashSet<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Round> getPlayedRounds() {
        return playedRounds;
    }

    public void setPlayedRounds(Set<Round> playedRounds) {
        this.playedRounds = playedRounds;
    }

    public Set<Round> getWonRounds() {
        return wonRounds;
    }

    public void setWonRounds(Set<Round> wonRounds) {
        this.wonRounds = wonRounds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKillsCount() {
        return killsCount;
    }

    public void setKillsCount(Integer killsCount) {
        this.killsCount = killsCount;
    }

    public Integer getDeathsCount() {
        return deathsCount;
    }

    public void setDeathsCount(Integer deathsCount) {
        this.deathsCount = deathsCount;
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
