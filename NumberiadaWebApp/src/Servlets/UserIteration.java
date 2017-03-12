/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import GameEngine.AppManager;
import GameEngine.GameManager;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.Point;
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
import java.util.List;

@WebServlet(name = "UserIteration", urlPatterns =
        {
                "/userIteration"   //"/User-Iteration"
        })
public class UserIteration extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        GameManager gameManager = (GameManager)session.getAttribute(Constants.GAME_MANAGER);
        boolean isComputer = (boolean)session.getAttribute(Constants.IS_COMPUTER);


        String message = "";
        Gson json = new Gson();
        PrintWriter out = response.getWriter();
        if (gameManager != null)
        {
            switch (request.getParameter(Constants.ACTION_TYPE))
            {
                case "userIteration":
                    message = gameManager.MoveAdvanceMove(makePointFromNum(request, gameManager));
                    out.print(json.toJson(message));
                    break;
                case "computerIteration":
                    gameManager.makeComputerMove();
                    out.print(json.toJson(message));
                    break;

                case "quit":
                    if(isComputer){
                        out.print(json.toJson(isComputer));
                    }else {
                        int playerIndex = Integer.parseInt(request.getParameter(Constants.PLAYER_INDEX));
                        gameManager.getGameLogic().getPlayers().get(playerIndex).setActive(false);
                        System.out.print(String.format("Retired Player index = %d name = %s \n", playerIndex, gameManager.getGameLogic().getPlayers().get(playerIndex).getName()));
                        System.out.print(String.format("I am disabled !! = %d --%s \n", playerIndex, gameManager.getGameLogic().getPlayers().get(playerIndex).getName()));
                        message = gameManager.AdvanceRetire();
                        session.removeAttribute(Constants.GAME_MANAGER);
                        session.removeAttribute(Constants.PLAYER_INDEX);
                        session.removeAttribute(Constants.GAME_NUMBER);
                        session.removeAttribute(Constants.GAME_TITLE);
                    }
                    break;
                default:
                    break;
            }

            gameManager.updateGameVersion();
            System.out.print("game Version after Itertation \n=" + gameManager.getGameVersion());
        }

        out.flush();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws java.io.IOException if an I/O error occurs
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
     * @throws java.io.IOException if an I/O error occurs
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

    private Point makePointFromNum(HttpServletRequest request, GameManager gameManager)
    {
        String row1 = request.getParameter(Constants.ROW);
        String col1 = request.getParameter(Constants.COL);
        int row = Integer.parseInt(row1);
        int col = Integer.parseInt(col1);
        return new Point(row, col);
    }
}
