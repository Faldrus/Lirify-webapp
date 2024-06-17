package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.InfoUserDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import it.unipd.dei.ranamelone.resources.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InfoUserRR extends AbstractRR {
    /**
     * Creates a new REST resource for info about User.
     * > GET rest/users/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public InfoUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.INFO_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Users obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int UserID = Integer.parseInt(path.substring(path.lastIndexOf("users") + 6));

            obj = new InfoUserDAO(con, UserID).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("User successfully read.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("User not found. Cannot read it.");

                m = new Message(String.format("User %d not found. Cannot read it.", UserID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot read the user: wrong format for URI /users/{ID}.", ex);

            m = new Message("Cannot read the users: wrong format for URI /users/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot read the users: unexpected database error.", ex);

            m = new Message("Cannot read the users: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}