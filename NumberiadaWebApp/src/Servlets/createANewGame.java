package Servlets;

//import chineseCheckers.GameManager;
//import gameSettings.GameSettings;
/*
@WebServlet(name = "createANewGame", urlPatterns =
{
    "/createANewGame"
})
public class createANewGame extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int numOfPlayers = Integer.parseInt(request.getParameter(Constants.NUM_OF_PLAYERS));
        int numOfColors = Integer.parseInt(request.getParameter(Constants.NUM_OF_COLORS));
        int numOfHumanPlayers = Integer.parseInt(request.getParameter(Constants.NUM_OF_HUMAN_PLAYERS));
        
        GameSettings gameSettings = new GameSettings(numOfPlayers, numOfColors, numOfHumanPlayers);
        int[] playersVersion = new int[numOfPlayers];
        GameManager game = new GameManager(gameSettings);
        SessionUtils.setGameManager(getServletContext(), game);
        SessionUtils.setPlayersVersion(getServletContext(), playersVersion);
        HttpSession playerSession = request.getSession(false);
        String name = playerSession.getAttribute(Constants.USERNAME).toString();
        boolean isComputer = Boolean.parseBoolean(request.getParameter(Constants.IS_COMPUTER));
        game.addPlayer(name, isComputer);
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
    /*@Override
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
    /*@Override
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
    /*@Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}*/
