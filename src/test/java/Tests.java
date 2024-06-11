import models.Match;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import services.MatchScoreCalculationService;


import javax.swing.text.PlainDocument;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {

    private MatchScoreCalculationService service;
    private Match match;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setup(){
        service = new MatchScoreCalculationService();
        player1= new Player();
        player1.setName("player1");
        player2 = new Player();
        player2.setName("player2");
        match = new Match();
        match.setPlayer1(player1);
        match.setPlayer2(player2);
        MatchScoreCalculationService.taiBreak=false;
    }

    @Test
    public void testPlayer1WinsPointAtDeuce(){
        match.setScore1(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        match.setScore2(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        service.score(match,"player1");
        assertEquals(MatchScoreCalculationService.ValuesScore.ADVANTAGE.getValue(),match.getScore1());
        assertEquals(MatchScoreCalculationService.ValuesScore.FORTY.getValue(),match.getScore2());
    }

    @Test
    public void testPlayer1WinsGameFromForty(){
        match.setScore1(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        match.setScore2(MatchScoreCalculationService.ValuesScore.ZERO.getValue());
        service.score(match,"player1");
        assertEquals(1,match.getScoreByGame1());
        assertEquals(0,match.getScoreByGame2());
        assertEquals(0,match.getScore1());
        assertEquals(0,match.getScore2());
    }

    @Test
    public void testTaiBreakIsTrue(){
        match.setScoreByGame1(6);
        match.setScoreByGame2(6);
        service.score(match,"player1");
        assertTrue(MatchScoreCalculationService.taiBreak);
    }

    @Test
    public void testPlayer1WinsTieBreak(){
        MatchScoreCalculationService.taiBreak=true;
        match.setScore1(6);
        match.setScore2(4);
        service.score(match,"player1");
        assertEquals(0,match.getScore1());
        assertEquals(0,match.getScore2());
        assertEquals(1,match.getScoreByGame1());
        assertEquals(0,match.getScoreByGame2());
    }

    @Test
    public void testPlayer1WinstPointInTieBreak(){
        MatchScoreCalculationService.taiBreak=true;
        service.score(match,"player1");
        assertEquals(1,match.getScore1());
        assertEquals(0,match.getScore2());
    }

    @Test
    public void testPlayer1WinsGameAfterDeuceAndAdvantage(){
        match.setScore1(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        match.setScore2(MatchScoreCalculationService.ValuesScore.ADVANTAGE.getValue());
        service.score(match,"player1");
        assertEquals(MatchScoreCalculationService.ValuesScore.FORTY.getValue(),match.getScore1());
        assertEquals(MatchScoreCalculationService.ValuesScore.FORTY.getValue(),match.getScore2());
        service.score(match,"player1");
        assertEquals(MatchScoreCalculationService.ValuesScore.ADVANTAGE.getValue(),match.getScore1());
        assertEquals(MatchScoreCalculationService.ValuesScore.FORTY.getValue(),match.getScore2());
        service.score(match,"player1");
        assertEquals(MatchScoreCalculationService.ValuesScore.ZERO.getValue(),match.getScore1());
        assertEquals(MatchScoreCalculationService.ValuesScore.ZERO.getValue(),match.getScore2());
        assertEquals(1,match.getScoreByGame1());
        assertEquals(0,match.getScoreByGame2());
    }

    @Test
    public void testPlayer1WinSet(){
        match.setScoreByGame1(5);
        match.setScoreByGame2(3);
        match.setScore1(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        match.setScore2(MatchScoreCalculationService.ValuesScore.THIRTY.getValue());
        service.score(match,"player1");
        assertEquals(1,match.getScoreBySet1());
        assertEquals(0,match.getScoreBySet2());
        assertEquals(0,match.getScoreByGame1());
        assertEquals(0,match.getScoreByGame2());
    }

    @Test
    public void testPlayer2WinsSet(){
        match.setScoreByGame2(5);
        match.setScoreByGame1(3);
        match.setScore2(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        match.setScore1(MatchScoreCalculationService.ValuesScore.THIRTY.getValue());
        service.score(match,"player2");
        assertEquals(1,match.getScoreBySet2());
        assertEquals(0,match.getScoreBySet1());
        assertEquals(0,match.getScoreByGame1());
        assertEquals(0,match.getScoreByGame2());
    }

    @Test
    public void testMatchWinnerPlayer1(){
        match.setScoreBySet1(1);
        match.setScoreBySet2(1);
        match.setScoreByGame1(5);
        match.setScoreByGame2(3);
        match.setScore1(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        match.setScore2(MatchScoreCalculationService.ValuesScore.THIRTY.getValue());
        service.WinAllSet(match,"player1");
        assertEquals(player1,match.getWinner());
    }

    @Test
    public void testMatchWinnerPlayer2(){
        match.setScoreBySet1(1);
        match.setScoreBySet2(1);
        match.setScoreByGame2(5);
        match.setScoreByGame1(3);
        match.setScore2(MatchScoreCalculationService.ValuesScore.FORTY.getValue());
        match.setScore1(MatchScoreCalculationService.ValuesScore.THIRTY.getValue());
        service.WinAllSet(match,"player2");
        assertEquals(player2,match.getWinner());
    }
}