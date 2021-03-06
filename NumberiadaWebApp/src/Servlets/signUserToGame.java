/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import GameEngine.AppManager;
import GameEngine.GameManager;
import GameEngine.validation.UserMessageConfirmation;
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


@WebServlet(name = "signUserToGame", urlPatterns =
        {
                "/signUserToGame"
        })
public class signUserToGame extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);

        String gameNumber = request.getParameter("gameNumber");
        String gameTitle = request.getParameter("gameTitle");

        session.setAttribute(Constants.GAME_TITLE, gameTitle);
        session.setAttribute(Constants.GAME_NUMBER,gameNumber);

       // SessionUtils.setGameNumber(getServletContext(), gameNumber);

        String userName = SessionUtils.getUsername(request);
        Boolean IsComputer = SessionUtils.getIsComputer(request);

       // SessionUtils.setGameManager(getServletContext(), AppManager.games.get(gameTitle));

        session.setAttribute(Constants.GAME_MANAGER, AppManager.games.get(gameTitle));
        UserMessageConfirmation messageConfirm = AppManager.SignToGame(Integer.parseInt(gameNumber),gameTitle,userName,IsComputer);

          int indexOfPlayer = messageConfirm.getPlayersIndex();
           session.setAttribute(Constants.PLAYER_INDEX,indexOfPlayer);
        //SessionUtils.setPlayerIndex(getServletContext(),indexOfPlayer);

        Gson json = new Gson();
        PrintWriter out = response.getWriter();
        out.print(json.toJson(messageConfirm));
        out.flush();
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
