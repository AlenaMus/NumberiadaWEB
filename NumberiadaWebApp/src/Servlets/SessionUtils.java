package Servlets;

import GameEngine.UsersManager;
import GameEngine.logic.GameLogic;
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

    public static UsersManager getUserManager(ServletContext servletContext)
    {
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null)
        {
            servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UsersManager());
        }
        return (UsersManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static GameLogic getGameManager(ServletContext servletContext)
    {
        return (GameLogic) servletContext.getAttribute(Constants.GAME_MANAGER);
    }

    public static void setGameManager(ServletContext servletContext, GameLogic gameLogic)
    {
        servletContext.setAttribute(Constants.GAME_MANAGER, gameLogic);
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
