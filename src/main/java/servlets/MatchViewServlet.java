package servlets;

import dao.MatchHibernate;
import org.hibernate.SessionFactory;
import services.FinishedMatchesPersistenceService;
import utils.HibernateSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MatchViewServlet extends HttpServlet{
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    public MatchViewServlet(){
        this.finishedMatchesPersistenceService= new FinishedMatchesPersistenceService(HibernateSessionFactory.getSessionFactory());
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path ="pages/match-view.jsp";
        req.setAttribute("allMatches",finishedMatchesPersistenceService.getMatches(req.getParameter("page"),req.getParameter("filter_by_player_name")));
        req.getRequestDispatcher(path).forward(req,resp);
    }
}