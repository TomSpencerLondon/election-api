package bbc.news.elections.model;

import java.math.BigDecimal;
import java.util.Map;

public class Scoreboard {
    private Map<String, Integer> seatsPerParty;
    private Map<String, Integer> totalVotesPerParty;
    private Map<String, BigDecimal> voteSharePerParty;
    private String overallWinner;


    public Map<String, Integer> getSeatsPerParty() {
        return seatsPerParty;
    }

    public void setSeatsPerParty(Map<String, Integer> seatsPerParty) {
        this.seatsPerParty = seatsPerParty;
    }

    public Map<String, Integer> getTotalVotesPerParty() {
        return totalVotesPerParty;
    }

    public void setTotalVotesPerParty(Map<String, Integer> totalVotesPerParty) {
        this.totalVotesPerParty = totalVotesPerParty;
    }

    public Map<String, BigDecimal> getVoteSharePerParty() {
        return voteSharePerParty;
    }

    public void setVoteSharePerParty(Map<String, BigDecimal> voteSharePerParty) {
        this.voteSharePerParty = voteSharePerParty;
    }

    public String getOverallWinner() {
        return overallWinner;
    }

    public void setOverallWinner(String overallWinner) {
        this.overallWinner = overallWinner;
    }
}
