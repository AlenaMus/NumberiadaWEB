/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import GameEngine.GameManager;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.ePlayerType;
import Servlets.Const.Constants;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name = "GetGamePlayers", urlPatterns = //"/Get-Game-Players"
{
    "/getGamePlayers"
})
public class GetGamePlayers extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        ResponseVariables responseVariables = new ResponseVariables();
        HttpSession session = request.getSession(false);
        GameManager game = (GameManager)session.getAttribute(Constants.GAME_MANAGER);                //SessionUtils.getGameManager(getServletContext());

        if(game != null) {
            responseVariables.players = game.getGameLogic().getActivePlayers();
            responseVariables.currPlayer = game.getGameLogic().getCurrentPlayer();
            responseVariables.numOfPlayers = responseVariables.players.size();
            responseVariables.myPlayerIndex = (Integer)session.getAttribute(Constants.PLAYER_INDEX);
            responseVariables.isComputer = (boolean)session.getAttribute(Constants.IS_COMPUTER);
            Gson json = new Gson();
            PrintWriter out = response.getWriter();
            out.print(json.toJson(responseVariables));
            out.flush();
        }
    }

    private class ResponseVariables
    {
        public List<Player> players = null;
        public Player currPlayer = null;
        public int numOfPlayers;
        public int myPlayerIndex;
        public boolean isComputer = false;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
