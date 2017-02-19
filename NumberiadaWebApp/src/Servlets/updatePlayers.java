package Servlets;

import GameEngine.UsersManager;
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

/**
 *
 * @author Kinneret & Anton
 */
@WebServlet(urlPatterns =
{
    "/updatePlayers"
})
public class updatePlayers extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            ResponseVariables responseVariables = new ResponseVariables();
            UsersManager userManager = SessionUtils.getUserManager(getServletContext());
            if (userManager != null)
            {
                List<String> users = userManager.getUsers();
                if (Integer.parseInt(request.getParameter("myPlayersVersion")) != users.size())
                {
                    responseVariables.players = users;
                }
                responseVariables.latestPlayersVersion = users.size();
            }
            Gson json = new Gson();
            PrintWriter out = response.getWriter();
            String t = json.toJson(responseVariables);
            out.print(json.toJson(responseVariables));
            out.flush();
        }
    }

    private class ResponseVariables
    {

        public int latestPlayersVersion;
        public List<String> players = null;
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
