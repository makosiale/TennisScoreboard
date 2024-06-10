package dao;

import models.Match;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchHibernate {
    private final SessionFactory sessionFactory;

    public MatchHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addMatch(Match match) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(match);
            transaction.commit();
        }
    }

    public List<Match> findAllMatches(int page) {
        List<Match> allMatches = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            allMatches = session.createQuery("From Match", Match.class).setFirstResult((page - 1) * 5).setMaxResults(5).list();
            transaction.commit();
        }
        return allMatches;
    }

    public List<Match> findMatchesByName(int page,String name){
        List<Match> allMatches = new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            String hql="SELECT m FROM Match m " +
                    "INNER JOIN m.player1 p1 " +
                    "INNER JOIN m.player2 p2 " +
                    "INNER JOIN m.winner w " +
                    "WHERE p1.name =:playerName OR p2.name = :playerName";
            Transaction transaction= session.beginTransaction();
            allMatches= session.createQuery(hql,Match.class).setFirstResult((page-1)*5)
                    .setMaxResults(5).setParameter("playerName",name).list();
            transaction.commit();
        }
        return allMatches;
    }
}
