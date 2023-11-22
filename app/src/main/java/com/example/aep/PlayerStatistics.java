package com.example.aep;

public class PlayerStatistics {
    private int runsScored;
    private int wicketsTaken;
    private int boundaries;
    private int oversBowled;
    private int ballsPlayed;
    private int catchesTaken;
    private int rating;

    private int strikeRate;
    private int totalCenturies;
    private int totalHalfCenturies;
    private int matchesPlayed;

    // Required empty constructor for Firestore
    public PlayerStatistics() {}

    public PlayerStatistics(int runsScored, int wicketsTaken, int boundaries,
                            int oversBowled, int ballsPlayed, int catchesTaken, int rating, int strikeRate, int totalCenturies, int totalHalfCenturies, int matchesPlayed) {
        this.runsScored = runsScored;
        this.wicketsTaken = wicketsTaken;
        this.boundaries = boundaries;
        this.oversBowled = oversBowled;
        this.ballsPlayed = ballsPlayed;
        this.catchesTaken = catchesTaken;
        this.rating = rating;
        this.strikeRate = strikeRate;
        this.totalCenturies = totalCenturies;
        this.totalHalfCenturies = totalHalfCenturies;
        this.matchesPlayed = matchesPlayed;
    }

    // Getter methods for each field
    public int getRunsScored() {
        return runsScored;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }

    public int getBoundaries() {
        return boundaries;
    }

    public int getOversBowled() {
        return oversBowled;
    }

    public int getBallsPlayed() {
        return ballsPlayed;
    }

    public int getCatchesTaken() {
        return catchesTaken;
    }

    public int getRating() {
        return rating;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    // Setter methods for each field
    public void setRunsScored(int runsScored) {
        this.runsScored = runsScored;
    }

    public void setWicketsTaken(int wicketsTaken) {
        this.wicketsTaken = wicketsTaken;
    }

    public void setBoundaries(int boundaries) {
        this.boundaries = boundaries;
    }

    public void setOversBowled(int oversBowled) {
        this.oversBowled = oversBowled;
    }

    public void setBallsPlayed(int ballsPlayed) {
        this.ballsPlayed = ballsPlayed;
    }

    public void setCatchesTaken(int catchesTaken) {
        this.catchesTaken = catchesTaken;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public int getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(int strikeRate) {
        this.strikeRate = strikeRate;
    }

    public int getTotalCenturies() {
        return totalCenturies;
    }

    public void setTotalCenturies(int totalCenturies) {
        this.totalCenturies = totalCenturies;
    }

    public int getTotalHalfCenturies() {
        return totalHalfCenturies;
    }

    public void setTotalHalfCenturies(int totalHalfCenturies) {
        this.totalHalfCenturies = totalHalfCenturies;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }
}
