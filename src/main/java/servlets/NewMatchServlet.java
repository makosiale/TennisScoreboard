package servlets;

import services.OngoingMatchesService;
import utils.HibernateSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class NewMatchServlet extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService;

    public NewMatchServlet() {
        this.ongoingMatchesService = new OngoingMatchesService(HibernateSessionFactory.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = "/pages/new-match.jsp";
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID path1=ongoingMatchesService.createMatch(req);
        String path="/match-score?uuid="+path1.toString();
        System.out.println(path);
        resp.sendRedirect(path);
    }
}
