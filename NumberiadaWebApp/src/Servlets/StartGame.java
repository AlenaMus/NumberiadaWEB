/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import GameEngine.GameManager;
import GameEngine.gameObjects.Player;
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

@WebServlet(name = "StartGame", urlPatterns =
        {
                "/startGame"
        })
public class StartGame extends HttpServlet
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Player currentPlayer = null;
        String message = "";
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        GameManager gameManager = (GameManager)session.getAttribute(Constants.GAME_MANAGER);  // SessionUtils.getGameManager(getServletContext());
        Gson json = new Gson();
        PrintWriter out = response.getWriter();
        int numOfPlayersToStartGame = gameManager.getGameLogic().getNumOfPlayers();
        int numOfSignedPlayers = gameManager.getGameLogic().getNumOfSignedPlayers();
        String userName = session.getAttribute(Constants.USERNAME).toString();
        if (numOfPlayersToStartGame == numOfSignedPlayers)
        {
            message = gameManager.findFirstPlayerToMove();
            currentPlayer = gameManager.getGameLogic().getCurrentPlayer();
            out.print(json.toJson(new ResponseVariables(true,message,currentPlayer,userName)));
        } else
        {
            out.print(json.toJson(new ResponseVariables(false, "There is not enough players to start the game",userName)));
        }
        out.flush();
    }

    private class ResponseVariables
    {

        public boolean succeedToStartGame;
        public String message;
        public Player currentPlayer;
        public String userName;

        public ResponseVariables(boolean succeedToStartGame,String message,Player currentPlayer,String userName)
        {
            this.succeedToStartGame = succeedToStartGame;
            this.currentPlayer = currentPlayer;
            this.message = message;
            this.userName = userName;
        }

        public ResponseVariables(boolean succeedToStartGame, String message,String userName)
        {
            this.succeedToStartGame = succeedToStartGame;
            this.message = message;
            this.userName = userName;
        }
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
