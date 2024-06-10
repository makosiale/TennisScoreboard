package servlets;

import models.Match;
import services.FinishedMatchesPersistenceService;
import services.MatchScoreCalculationService;
import services.OngoingMatchesService;
import utils.HibernateSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class MatchScoreServlet extends HttpServlet {
    private final OngoingMatchesService ongoingMatchesService;

    public MatchScoreServlet() {
        this.ongoingMatchesService = new OngoingMatchesService(HibernateSessionFactory.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = "/pages/match-score.jsp";
        req.setAttribute("match", OngoingMatchesService.startedMatches.get(UUID.fromString(req.getParameter("uuid"))));
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String playerWinScore = req.getParameter("submit");
            String matchUuid = req.getParameter("uuid");

            if (matchUuid == null || playerWinScore == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }

            Match match = OngoingMatchesService.startedMatches.get(UUID.fromString(matchUuid));

            if (match == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Match not found");
                return;
            }

            MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
            matchScoreCalculationService.WinAllSet(match, playerWinScore);

            logInfo(match);

            if (match.getWinner()!=null){
                OngoingMatchesService.startedMatches.remove(req.getParameter("uuid"));
                FinishedMatchesPersistenceService finishedMatchesPersistenceService= new FinishedMatchesPersistenceService(HibernateSessionFactory.getSessionFactory());
                finishedMatchesPersistenceService.addMatch(match);
            }

            String path = "/pages/match-score.jsp";
            req.setAttribute("match", match);
            req.getRequestDispatcher(path).forward(req, resp);

        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID format");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    private void logInfo(Match match) {
        System.out.println("Score 1: " + match.getScore1());
        System.out.println("Score 2: " + match.getScore2());
    }
}
