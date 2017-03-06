package Servlets;

import GameEngine.AppManager;
import GameEngine.gameObjects.Board;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.ePlayerType;
import Servlets.Const.Constants;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(urlPatterns = {"/getGameBoard" })

public class getGameBoard extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        Gson json = new Gson();
        PrintWriter out = response.getWriter();
        if (session != null) {
           String username = (String)session.getAttribute(Constants.USERNAME);
            Boolean isComp = (boolean)session.getAttribute(Constants.IS_COMPUTER);
            ePlayerType type = isComp ? ePlayerType.Computer:ePlayerType.Human;
            Board board =  AppManager.getGameForUser(new Player(username,type)).getGameBoard();
            if(board != null)
            {
                out.print(json.toJson(board));
                out.flush();

            }else{
                out.print(json.toJson("Error"));
                out.flush();
            }
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


}
