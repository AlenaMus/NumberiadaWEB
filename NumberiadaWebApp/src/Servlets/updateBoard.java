package Servlets;

import GameEngine.AppManager;
import GameEngine.gameObjects.Point;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "UpdateBoard", urlPatterns = {"/updateBoard" })

public class updateBoard extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String gameTitle;

        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        if (session != null) {
            gameTitle = request.getParameter("gameTitle");
            Gson json = new Gson();
            PrintWriter out = response.getWriter();
            out.print(json.toJson(AppManager.games.get(gameTitle).getGameLogic().getGameBoard()));
            out.flush();
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }



}