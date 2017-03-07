package Servlets;

import GameEngine.AppManager;
import GameEngine.UsersManager;
import GameEngine.gameObjects.Game;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns =
        {
                "/updateGamesTable"
        })

public class updateGamesTable extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            updateGamesTable.ResponseVariables responseVariables = new updateGamesTable.ResponseVariables();
            //UsersManager userManager = SessionUtils.getUserManager(getServletContext());

            if (AppManager.gamesInfo != null)
            {
                List<Game> games = AppManager.gamesInfo;

                if (Integer.parseInt(request.getParameter("myGamesListVersion")) != games.size())
                {
                    responseVariables.games = games;
                }
                responseVariables.listSize = games.size();
                responseVariables.latestGameVersion = games.size();
            }else{
                responseVariables.latestGameVersion = 0;
            }
            Gson json = new Gson();
            PrintWriter out = response.getWriter();
            out.print(json.toJson(responseVariables));
            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    private class ResponseVariables
    {
        public int latestGameVersion;
        public int listSize = 0;
        public List<Game> games = null;//new ArrayList<>();
    }


}
