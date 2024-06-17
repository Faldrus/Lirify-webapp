package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.ListAlbumsLikedByUserDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.ResourceList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListAlbumsLikedByUserRR extends AbstractRR {
    /**
     * Creates a new REST resource fo listing Albums liked by User.
     * > GET rest/users/{ID}/albums
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public ListAlbumsLikedByUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_ALBUMS_LIKED_BY_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Albums> objs = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int UserID = Integer.parseInt(path.substring(path.lastIndexOf("users") + 6, path.lastIndexOf("albums") - 1));

            objs = new ListAlbumsLikedByUserDAO(con, UserID).access().getOutputParam();

            if (objs != null) {
                LOGGER.info(String.format("Album(s) liked by user with id %d successfully read.", UserID));

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(objs).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("No album(s) found. Cannot read them.");

                m = new Message("No album(s) found. Cannot read them.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot search album(s): wrong format for URI /users/{ID}/albums.", ex);

            m = new Message("Cannot search album(s): wrong format for URI /users/{ID}/albums.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot search album(s): unexpected database error.", ex);

            m = new Message("Cannot search album(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}