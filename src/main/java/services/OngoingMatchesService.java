package services;

import dao.PlayerHibernate;
import models.Match;
import models.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import utils.HibernateSessionFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private final SessionFactory sessionFactory;

    public OngoingMatchesService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public static ConcurrentHashMap<UUID,Match> startedMatches = new ConcurrentHashMap<UUID,Match>();

    public UUID createMatch(HttpServletRequest req) {
        PlayerHibernate playerHibernate = new PlayerHibernate(sessionFactory);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            if (playerHibernate.getPlayer(req.getParameter("player1")).isEmpty()) {
                playerHibernate.addPlayer(req.getParameter("player1"));
            }
            if (playerHibernate.getPlayer(req.getParameter("player2")).isEmpty()) {
                playerHibernate.addPlayer(req.getParameter("player2"));
            }
            transaction.commit();

            Match match = new Match();
            match.setPlayer1(playerHibernate.getPlayer(req.getParameter("player1")).get());
            match.setPlayer2(playerHibernate.getPlayer(req.getParameter("player2")).get());
            UUID uuid= UUID.randomUUID();
            startedMatches.put(uuid,match);
            return uuid;
        }
    }
}
