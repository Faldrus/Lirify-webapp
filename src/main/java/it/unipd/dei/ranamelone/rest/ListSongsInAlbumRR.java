package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.ListSongsByAlbumDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Songs;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.ResourceList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListSongsInAlbumRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing Songs in Album.
     * GET rest/songs/albums/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public ListSongsInAlbumRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_SONGS_BY_ALBUM, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Songs> objs = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("albums") + 6);
            final int AlbumID = Integer.parseInt(path.substring(1));

            objs = new ListSongsByAlbumDAO(con, AlbumID).access().getOutputParam();

            if (objs != null) {
                LOGGER.info(String.format("Song(s) successfully searched by album with id %d.", AlbumID));

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(objs).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("No song(s) found. Cannot read them.");

                m = new Message("No song(s) found. Cannot read them.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot search song(s): wrong format for URI /songs/albums/{ID}.", ex);

            m = new Message("Cannot search songs(s): wrong format for URI /songs/albums/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot search song(s): unexpected database error.", ex);

            m = new Message("Cannot search song(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}