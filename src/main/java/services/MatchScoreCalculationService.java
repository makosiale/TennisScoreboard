package services;

import models.Match;
import models.Player;

public class MatchScoreCalculationService {

    public enum ValuesScore {
        ZERO(0),
        FIFTEEN(15),
        THIRTY(30),
        FORTY(40),
        ADVANTAGE(50);
        private final int value;

        ValuesScore(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static boolean taiBreak = false;

    public void score(Match match, String playerWinScore) {
        if (!taiBreak) isTaiBreak(match);
        if (taiBreak){
            handleTiebreak(match, playerWinScore);
        } else {
            handleRegularGame(match, playerWinScore);
        }
        checkSetWin(match);
    }

    private void isTaiBreak(Match match) {
        if (match.getScoreByGame1() == 6 && match.getScoreByGame2() == 6) {
            taiBreak = true;
            resetScoresForNewGame(match);
        }
    }

    private void handleTiebreak(Match match, String playerWinScore) {
        if (playerWinScore.equals("player1")) {
            match.setScore1(match.getScore1() + 1);
        } else if (playerWinScore.equals("player2")) {
            match.setScore2(match.getScore2() + 1);
        }

        if (match.getScore1() >= 7 && match.getScore1() - match.getScore2() >= 2) {
            match.setScoreByGame1(match.getScoreByGame1() + 1);
            resetScoresForNewGame(match);
        } else if (match.getScore2() >= 7 && match.getScore2() - match.getScore1() >= 2) {
            match.setScoreByGame2(match.getScoreByGame2() + 1);
            resetScoresForNewGame(match);
        }
    }

    private void handleRegularGame(Match match, String playerWinScore) {
        if (match.getScore1() >= ValuesScore.FORTY.getValue() && match.getScore2() >= ValuesScore.FORTY.getValue()) {
            handleDeuce(match, playerWinScore);
        } else {
            handleNormalScoring(match, playerWinScore);
        }
    }

    private void handleDeuce(Match match, String playerWinScore) {
        if (playerWinScore.equals("player1")) {
            if (match.getScore2() == ValuesScore.ADVANTAGE.getValue())
                match.setScore2(ValuesScore.FORTY.getValue());
            else if (match.getScore1() == ValuesScore.FORTY.getValue()) {
                match.setScore1(ValuesScore.ADVANTAGE.getValue());
            } else if (match.getScore1() == ValuesScore.ADVANTAGE.getValue()) {
                match.setScoreByGame1(match.getScoreByGame1() + 1);
                resetScoresForNewGame(match);
            } else {
                match.setScore1(ValuesScore.FORTY.getValue());
                match.setScore2(ValuesScore.FORTY.getValue());
            }
        } else if (playerWinScore.equals("player2")) {
            if (match.getScore1() == ValuesScore.ADVANTAGE.getValue())
                match.setScore1(ValuesScore.FORTY.getValue());
            else if (match.getScore2() == ValuesScore.FORTY.getValue()) {
                match.setScore2(ValuesScore.ADVANTAGE.getValue());
            } else if (match.getScore2() == ValuesScore.ADVANTAGE.getValue()) {
                match.setScoreByGame2(match.getScoreByGame2() + 1);
                resetScoresForNewGame(match);
            } else {
                match.setScore1(ValuesScore.FORTY.getValue());
                match.setScore2(ValuesScore.FORTY.getValue());
            }
        }
    }

    private void handleNormalScoring(Match match, String playerWinScore) {
        if (playerWinScore.equals("player1")) {
            if (match.getScore1() == ValuesScore.THIRTY.getValue()) {
                match.setScore1(ValuesScore.FORTY.getValue());
            } else if (match.getScore1() == ValuesScore.FORTY.getValue()) {
                match.setScoreByGame1(match.getScoreByGame1() + 1);
                resetScoresForNewGame(match);
            } else {
                match.setScore1(match.getScore1() + ValuesScore.FIFTEEN.getValue());
            }
        } else if (playerWinScore.equals("player2")) {
            if (match.getScore2() == ValuesScore.THIRTY.getValue()) {
                match.setScore2(ValuesScore.FORTY.getValue());
            } else if (match.getScore2() == ValuesScore.FORTY.getValue()) {
                match.setScoreByGame2(match.getScoreByGame2() + 1);
                resetScoresForNewGame(match);
            } else {
                match.setScore2(match.getScore2() + ValuesScore.FIFTEEN.getValue());
            }
        }
    }

    private void resetScoresForNewGame(Match match) {
        match.setScore1(ValuesScore.ZERO.getValue());
        match.setScore2(ValuesScore.ZERO.getValue());
    }

    private void checkSetWin(Match match) {
        if (taiBreak) {
            if (match.getScoreByGame1() == 7) {
                match.setScoreBySet1(match.getScoreBySet1() + 1);
                resetGameScores(match);
                taiBreak=false;
            }
            else if (match.getScoreByGame2() == 7) {
                match.setScoreBySet2(match.getScoreBySet2() + 1);
                resetGameScores(match);
                taiBreak=false;
            }
        } else {
            if (match.getScoreByGame1() >= 6 && match.getScoreByGame1() - match.getScoreByGame2() >= 2) {
                match.setScoreBySet1(match.getScoreBySet1() + 1);
                resetGameScores(match);
            } else if (match.getScoreByGame2() >= 6 && match.getScoreByGame2() - match.getScoreByGame1() >= 2) {
                match.setScoreBySet2(match.getScoreBySet2() + 1);
                resetGameScores(match);
            }
        }
    }

    private void resetGameScores(Match match) {
        match.setScoreByGame1(0);
        match.setScoreByGame2(0);
    }

    public void WinAllSet(Match match, String playerWinScore) {
        score(match, playerWinScore);
        if (match.getScoreBySet1() == 2) {
            match.setWinner(match.getPlayer1());
        } else if (match.getScoreBySet2() == 2) {
            match.setWinner(match.getPlayer2());
        }
    }
}
