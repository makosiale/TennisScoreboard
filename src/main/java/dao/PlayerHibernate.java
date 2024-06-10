package dao;

import models.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class PlayerHibernate {
    private final SessionFactory sessionFactory;

    public PlayerHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Player> getPlayer(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Player> query = session.createQuery("FROM Player WHERE name=:name", Player.class);
            query.setParameter("name", name);
            return query.uniqueResultOptional();
        }
    }

    public void addPlayer(String name) {
        try (Session session = sessionFactory.openSession()) {
            Player player = new Player();
            player.setName(name);
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
        }
    }
}
