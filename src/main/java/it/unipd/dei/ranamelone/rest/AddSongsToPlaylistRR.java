package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.AddSongsToPlaylistDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Playlists;
import it.unipd.dei.ranamelone.resources.Songs;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AddSongsToPlaylistRR extends AbstractRR {
    /**
     * Creates a new REST resource for adding Song to Playlist.
     * > POST rest/songs/{ID}/playlists/{ID}
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public AddSongsToPlaylistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.ADD_SONGS_TO_PLAYLIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        boolean result = false;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int SongID = Integer.parseInt(path.substring(path.lastIndexOf("songs") + 6 , path.lastIndexOf("playlists") - 1));
            final int PlaylistID = Integer.parseInt(path.substring(path.lastIndexOf("playlists") + 10));

            result = new AddSongsToPlaylistDAO(con, PlaylistID, SongID).access().getOutputParam();

            if (result) {
                LOGGER.info("Song successfully added to playlist.");

                res.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                LOGGER.error("Fatal error while adding song to playlist.");

                m = new Message("Cannot add song to playlist: unexpected error.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (NumberFormatException ex) {
            LOGGER.warn("Cannot add song to playlist: invalid playlist ID or song ID.", ex);

            m = new Message("Cannot add song to playlist: invalid playlist ID or song ID.", "E4A6", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot add song to playlist: song already exists in playlist.");

                m = new Message("Cannot add song to playlist: song already exists in playlist.", "E5A2", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot add song to playlist: unexpected database error.", ex);

                m = new Message("Cannot add song to playlist: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}