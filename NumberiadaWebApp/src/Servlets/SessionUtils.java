package Servlets;

import GameEngine.GameManager;
import GameEngine.UsersManager;
import Servlets.Const.Constants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class SessionUtils
{
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "usersManager";

    public static String getUsername(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Boolean getIsComputer(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.IS_COMPUTER) : null;
        return sessionAttribute != null ?( Boolean)sessionAttribute: null;
    }


    public static UsersManager getUserManager(ServletContext servletContext)
    {
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null)
        {
            servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UsersManager());
        }
        return (UsersManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static GameManager getGameManager(ServletContext servletContext)
    {
        return (GameManager) servletContext.getAttribute(Constants.GAME_MANAGER);
    }

    public static void setGameManager(ServletContext servletContext, GameManager gameManager)
    {
        servletContext.setAttribute(Constants.GAME_MANAGER, gameManager);
    }
    public static String getGameTitle(ServletContext servletContext)
    {
        return (String) servletContext.getAttribute(Constants.GAME_TITLE);
    }

    public static void setGameTitle(ServletContext servletContext, String gameTitle)
    {
        servletContext.setAttribute(Constants.GAME_TITLE, gameTitle);
    }

    public static void setGameNumber(ServletContext servletContext, String gameNumber){
        servletContext.setAttribute(Constants.GAME_NUMBER, gameNumber);
    }

    public static int getGameNumber(ServletContext servletContext){
        return (int) servletContext.getAttribute(Constants.GAME_NUMBER);
    }

    public static void clearGameManager(ServletContext servletContext)
    {
        servletContext.removeAttribute(Constants.GAME_MANAGER);
    }

    public static void clearSession(HttpServletRequest request)
    {
        request.getSession().invalidate();
    }

    public static void setPlayersVersion(ServletContext servletContext, int[] playersVersion)
    {
        initArray(playersVersion);
        servletContext.setAttribute(Constants.PLAYERS_VERSION, playersVersion);
    }

    public static int[] getPlayersVersion(ServletContext servletContext)
    {
        return (int[]) servletContext.getAttribute(Constants.PLAYERS_VERSION);
    }

    public static void setPlayerVersionByIndex(ServletContext servletContext, int playerIndex, int playerVersion)
    {
        getPlayersVersion(servletContext)[playerIndex] = playerVersion;
    }

    private static void initArray(int[] playersVersion)
    {
        for (int player : playersVersion)
        {
            player = 0;
        }
    }

}
