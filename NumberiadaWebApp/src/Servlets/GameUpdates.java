/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import GameEngine.GameManager;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.Square;
import Servlets.Const.Constants;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "GameUpdates", urlPatterns =
        {
                "/Game-Updates"
        })
public class GameUpdates extends HttpServlet
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        GameManager gameManager = SessionUtils.getGameManager(getServletContext());
        JasonResponse jasonResponse = new JasonResponse();
        if (gameManager != null)
        {
            int playerVersion = Integer.parseInt(request.getParameter(Constants.PLAYER_VERSION));
           // int playerIndex = Integer.parseInt(request.getParameter(Constants.PLAYER_INDEX));


            if (isPlayerUpToDate(playerVersion, playerIndex, gameManager))
            {
                getUpdatesFromGame(gameManager, jasonResponse, response);

            } else
            {
                writeJasonRespons(false, response);
            }
        }
    }

    private void clearData(GameManager gameEngine)
    {
//        gameEngine.clearCellsToUpdate();
//        gameEngine.clearDeletedPlayerIndex();
//        if (gameEngine.isGameOver())
//        {
//            SessionUtils.clearGameManager(getServletContext());
//        }
    }

    private void getUpdatesFromGame(GameManager gameManager, JasonResponse jasonResponse, HttpServletResponse response) throws IOException
    {
        jasonResponse.gameOver = gameManager.getGameLogic().isGameOver();
        if (gameManager.getGameLogic().isGameOver())
        {
            jasonResponse.winner = gameManager.setGameOver();
        }

       // jasonResponse.DeletedPlayerIndex = gameManager.getDeletedPlayerIndex();
        jasonResponse.cellToUpdate = gameManager.getGameLogic().getSquaresToUpdate();
        jasonResponse.currentPlayer = gameManager.getGameLogic().getCurrentPlayer();
        jasonResponse.latestGameVersion = gameManager.getGameVersion();
        jasonResponse.computerTurn = gameManager.isComputerTurn();
        jasonResponse.allPlayersAreUpToDate = areAllPlayersUpTODate(SessionUtils.getPlayersVersion(getServletContext()), gameManager);
        writeJasonRespons(jasonResponse, response);
        if (jasonResponse.allPlayersAreUpToDate)
        {
            clearData(gameManager);
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

    private boolean isPlayerUpToDate(int playerVersion, int playerIndex, GameManager gameManager)
    {
        if (gameManager.getGameVersion() > playerVersion)
        {
            int index = Integer.parseInt(String.valueOf(SessionUtils.getPlayerIndex(getServletContext())));
            gameManager.getGameLogic().getPlayers().get(index).setPlayerVersion(gameManager.getGameVersion());
            //SessionUtils.setPlayerVersionByIndex(getServletContext(), playerIndex, playerVersion + 1);
            return true;
        }
        return false;
    }

    private void writeJasonRespons(Object jasonResponse, HttpServletResponse response) throws IOException
    {
        try (PrintWriter out = response.getWriter())
        {
            Gson json = new Gson();
            out.print(json.toJson(jasonResponse));
        }
    }

    private boolean areAllPlayersUpTODate(int[] playersVersion, GameManager gameEngine)
    {
        boolean isSuccess = true;
        for (int i = 0; i < playersVersion.length; i++)
        {
           // if (gameEngine.isPlayerByIndexIsHuman(i) && playersVersion[i] != gameEngine.getGameVersion())
           // {
           //     isSuccess = false;
           // }
        }
        return isSuccess;
    }

    private class JasonResponse
    {
        public int latestGameVersion;
        public Player currentPlayer;
        //public int DeletedPlayerIndex = -1;
        public int lastPlayerIndexWhoQuit = -1; //should be the index of the player who did quit.
        public boolean allPlayersAreUpToDate = false;
        public String winner;
        public boolean gameOver;
        public boolean computerTurn;
        public List<Square> cellToUpdate;
    }
}