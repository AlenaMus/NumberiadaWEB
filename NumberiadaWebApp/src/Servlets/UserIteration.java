/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

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
        GameManager gameManager = SessionUtils.getGameManager(getServletContext());
        String message="";
        GameManager.NextPlayerMove  nextPlayerMove = null;
        List<Player> noMovePlayers;
        Gson json = new Gson();
        PrintWriter out = response.getWriter();
        if (gameManager != null)
        {
            switch (request.getParameter(Constants.ACTION_TYPE))
            {
                case "userIteration":
                    message = gameManager.MoveAdvanceMove(makePointFromNum(request, gameManager));
                    break;
                case "computerIteration":
                    gameManager.makeComputerMove();
                    break;

                case "quit":
                    message = gameManager.AdvanceRetire();
                   // out.print(json.toJson(noMovePlayers));
                    break;
               /* case "endTurn":
                    gameManager.endTurn();
                    break;*/
                default:
                    break;
            }

            gameManager.updateGameVersion();
            System.out.print(gameManager.getGameVersion());
        }
        out.print(json.toJson(message));
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
