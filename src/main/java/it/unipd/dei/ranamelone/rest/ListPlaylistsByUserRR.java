package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.ListPlaylistsByUserDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Playlists;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.ResourceList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListPlaylistsByUserRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing Playlists by User.
     * GET rest/playlists/users/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public ListPlaylistsByUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_PLAYLIST_BY_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Playlists> objs = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("users") + 5);
            final int UserID = Integer.parseInt(path.substring(1));

            objs = new ListPlaylistsByUserDAO(con, UserID).access().getOutputParam();

            if (objs != null) {
                LOGGER.info(String.format("Playlist(s) successfully searched by user with id %d.", UserID));

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(objs).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("No playlist(s) found. Cannot read them.");

                m = new Message("No playlist(s) found. Cannot read them.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot search playlist(s): wrong format for URI /playlists/users/{ID}.", ex);

            m = new Message("Cannot search playlist(s): wrong format for URI /playlists/users/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot search playlist(s): unexpected database error.", ex);

            m = new Message("Cannot search playlist(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}