package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.InfoPlaylistDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import it.unipd.dei.ranamelone.resources.Playlists;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InfoPlaylistRR extends AbstractRR {
    /**
     * Creates a new REST resource for info about Playlist.
     * > GET rest/playlists/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public InfoPlaylistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.INFO_PLAYLIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Playlists obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int PlaylistID = Integer.parseInt(path.substring(path.lastIndexOf("playlists") + 10));

            obj = new InfoPlaylistDAO(con, PlaylistID).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("Playlist successfully read.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Playlist not found. Cannot read it.");

                m = new Message(String.format("Playlist %d not found. Cannot read it.", PlaylistID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot read the playlist: wrong format for URI /playlist/{ID}.", ex);

            m = new Message("Cannot read the playlist: wrong format for URI /playlist/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot read the playlist: unexpected database error.", ex);

            m = new Message("Cannot read the playlist: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}