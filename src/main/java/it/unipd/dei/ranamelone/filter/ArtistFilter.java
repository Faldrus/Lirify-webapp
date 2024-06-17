package it.unipd.dei.ranamelone.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;

public class ArtistFilter extends AbstractFilter{

    final static Logger logger = LogManager.getLogger(ArtistFilter.class);


    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/jsp/login.jsp";
        String unauthorizedPage = req.getContextPath() + "/jsp/unauthorized.jsp";

        boolean loggedIn = session != null && session.getAttribute("id") != null;


        if (loggedIn) {
            if (session.getAttribute("name")!=null) { // if the session has a name it is from an artist because the users not have name attribute
                res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                chain.doFilter(req, res); // User is logged in, just continue request.
            }
            else {
                logger.info("user "+session.getAttribute("id")+
                        " that it's not an artists tried to access the artist page");
                res.sendRedirect(unauthorizedPage); //Not authorized, show the proper page
            }
        } else {
            res.sendRedirect(loginURI); // Not logged in, show login page.
        }
    }

}
