package bbc.news.elections.controllers;

import bbc.news.elections.model.ConstituencyResult;
import bbc.news.elections.model.PartyResult;
import bbc.news.elections.model.Scoreboard;
import bbc.news.elections.service.NotImplementedException;
import bbc.news.elections.service.ResultNotFoundException;
import bbc.news.elections.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ResultsController {

    private final ResultService resultService;

    public ResultsController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/result/{id}")
    ConstituencyResult getResult(@PathVariable Integer id) {
        ConstituencyResult result = resultService.GetResult(id);
        if (result == null) {
            throw new ResultNotFoundException(id);
        }
        return resultService.GetResult(id);
    }

    @PostMapping("/result")
    ResponseEntity<String> newResult(@RequestBody ConstituencyResult result) {
        if (result.getId() != null) {
            resultService.NewResult(result);
            return ResponseEntity.created(URI.create("/result/" + result.getId())).build();
        }
        return ResponseEntity.badRequest().body("Id was null");
    }

    @GetMapping("/scoreboard")
    Scoreboard getScoreboard() {
        Map<Integer, ConstituencyResult> results = resultService.GetAll();
        Map<String, Integer> seatsPerParty = new HashMap<>();
        Map<String, Integer> totalVotesPerParty = new HashMap<>();
        Map<String, BigDecimal> voteSharePerParty = new HashMap<>();
        int totalVotes = 0;

        // Calculate seats and votes
        for (ConstituencyResult result : results.values()) {
            PartyResult winner = result.getPartyResults().stream()
                    .max(Comparator.comparingInt(PartyResult::getVotes))
                    .orElse(null);
            if (winner != null) {
                // Increment seats for the winning party
                seatsPerParty.put(winner.getParty(),
                        seatsPerParty.getOrDefault(winner.getParty(), 0) + 1);
            }

            // Calculate total votes per party
            for (PartyResult partyResult : result.getPartyResults()) {
                String party = partyResult.getParty();
                int votes = partyResult.getVotes();
                totalVotesPerParty.put(party, totalVotesPerParty.getOrDefault(party, 0) + votes);
                totalVotes += votes;
            }
        }

        // Calculate vote share
        for (Map.Entry<String, Integer> entry : totalVotesPerParty.entrySet()) {
            String party = entry.getKey();
            int votes = entry.getValue();
            BigDecimal share = BigDecimal.valueOf((votes * 100.0) / totalVotes).setScale(2, RoundingMode.HALF_UP);
            voteSharePerParty.put(party, share);
        }

        // Determine overall winner
        String overallWinner = seatsPerParty.entrySet().stream()
                .filter(entry -> entry.getValue() >= 325)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        // Populate Scoreboard
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.setSeatsPerParty(seatsPerParty);
        scoreboard.setTotalVotesPerParty(totalVotesPerParty);
        scoreboard.setVoteSharePerParty(voteSharePerParty);
        scoreboard.setOverallWinner(overallWinner);

        return scoreboard;
    }
}
