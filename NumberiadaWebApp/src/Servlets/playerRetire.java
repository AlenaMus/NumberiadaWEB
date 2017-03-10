/*package Servlets;

import GameEngine.GameManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "playerRetire", urlPatterns =
        {
                "/playerRetire"
        })
public class playerRetire extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        GameManager gameManager = SessionUtils.getGameManager(getServletContext());
        if (gameManager != null)
    {
        String message =  gameManager.AdvanceRetire();
        Gson json = new Gson();
        PrintWriter out = response.getWriter();
        out.print(json.toJson(message));
        out.flush();
    }


    }}*/