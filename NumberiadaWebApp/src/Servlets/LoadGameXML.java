package Servlets;

import GameEngine.AppManager;
import GameEngine.GameManager;
import Servlets.Const.Constants;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


@WebServlet(name = "LoadGameXML", urlPatterns = {"/loadGameXML" })
@MultipartConfig

public class LoadGameXML extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Gson json = new Gson();
        PrintWriter out = response.getWriter();
        String userName = SessionUtils.getUsername(request);
        response.setContentType("application/json");
        GameManager gameManager = new GameManager();
        SessionUtils.setGameManager(getServletContext(), gameManager);

            Part filePart = request.getPart(Constants.LOAD_GAME_FILE);
            InputStream loadGameFile = filePart.getInputStream();

            try
            {
                gameManager.LoadGameFromXmlAndValidate(loadGameFile);
                AppManager.AddNewGame(gameManager,gameManager.getGameTitle());
                if(!GameManager.gameExists){
                    GameManager.gameExists = true;
                    out.print(json.toJson(gameManager.getNewGameData(userName)));
                    out.flush();
                }else{
                    out.print(json.toJson("There is already an existing game..."));
                    out.flush();
                }

            } catch (Exception ex){
                out.print(json.toJson(ex.getMessage()));
                out.flush();
            }

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
}



