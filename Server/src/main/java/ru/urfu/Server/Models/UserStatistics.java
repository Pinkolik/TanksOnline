package ru.urfu.Server.Models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users_statistics")
public class UserStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer killsCount;
    private Integer deathsCount;
    private Integer wonRoundsCount;
    private Integer lostRoundsCount;
    private Integer playedRoundsCount;
    private Integer firedShotsCount;
    private Integer madeMovesCount;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(
            name ="players_rounds",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "round_id")
    )
    private Set<Round> playedRounds;
    @OneToMany(mappedBy = "winner")
    private Set<Round> wonRounds;

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

    public Integer getWonRoundsCount() {
        return wonRoundsCount;
    }

    public void setWonRoundsCount(Integer wonRoundsCount) {
        this.wonRoundsCount = wonRoundsCount;
    }

    public Integer getLostRoundsCount() {
        return lostRoundsCount;
    }

    public void setLostRoundsCount(Integer lostRoundsCount) {
        this.lostRoundsCount = lostRoundsCount;
    }

    public Integer getPlayedRoundsCount() {
        return playedRoundsCount;
    }

    public void setPlayedRoundsCount(Integer playedRoundsCount) {
        this.playedRoundsCount = playedRoundsCount;
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
