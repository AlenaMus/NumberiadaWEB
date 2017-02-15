/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import GameEngine.UsersManager;
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

/**
 *
 * @author Anton
 */
@WebServlet(name = "login", urlPatterns
        = {
            "/login"
        })
public class Login extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(true);
        boolean usernameExists = false, loginPromot = false, loginSuccess = false;
         
        if (session.isNew()) {
            UsersManager userManager = SessionUtils.getUserManager(getServletContext());
            String userName = request.getParameter(Constants.USERNAME);
            boolean isComputer = Boolean.parseBoolean(request.getParameter(Constants.IS_COMPUTER));
            if (userName.equals("")){
                session.invalidate();
                loginPromot = true;
            }
            else if (userManager.isUserExists(userName)) {
                session.invalidate();
                usernameExists = true;
                loginPromot = true;
            } else {
                loginSuccess = true;
                session.setMaxInactiveInterval(0);
                session.setAttribute(Constants.USERNAME, userName);
                session.setAttribute(Constants.IS_COMPUTER, isComputer);
                userManager.addUser(userName);
            }
        } else {
            loginSuccess = true;
        }
        PrintWriter out = response.getWriter();
        Gson json = new Gson();
        ResponseVariables responseVariable = new ResponseVariables(usernameExists, loginPromot, loginSuccess);
        out.print(json.toJson(responseVariable));
        out.flush();
    }

    private class ResponseVariables {
        private boolean usernameExists, loginPromot, loginSuccess;

        public ResponseVariables(boolean usernameExists, boolean loginPromot, boolean loginSuccess) {
            this.usernameExists = usernameExists;
            this.loginPromot = loginPromot;
            this.loginSuccess = loginSuccess;
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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
