package services;

import dao.MatchHibernate;
import models.Match;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
import java.util.List;

public class FinishedMatchesPersistenceService {
    private final SessionFactory sessionFactory;

    public FinishedMatchesPersistenceService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addMatch(Match match) {
        MatchHibernate matchHibernate = new MatchHibernate(sessionFactory);
        matchHibernate.addMatch(match);
    }

    public List<Match> getMatches(String pageString, String name){
        MatchHibernate matchHibernate = new MatchHibernate(sessionFactory);
        int page = 1;
        if (pageString!=null){
            page = Integer.parseInt(pageString);
        }
        if (name ==null){
            return matchHibernate.findAllMatches(page);
        }else
            return matchHibernate.findMatchesByName(page,name);
    }
}
