package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.DeleteSongFromPlaylistDAO;
import it.unipd.dei.ranamelone.resources.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteSongFromPlaylistRR extends AbstractRR {
    /**
     * Creates a new REST resource.
     * > DELETE rest/songs/{ID}/playlists/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public DeleteSongFromPlaylistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_SONG_FROM_PLAYLIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Playlists obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int SongID = Integer.parseInt(path.substring(path.lastIndexOf("songs") + 6 , path.lastIndexOf("playlists") - 1));
            final int PlaylistID = Integer.parseInt(path.substring(path.lastIndexOf("playlists") + 10));

            obj = new DeleteSongFromPlaylistDAO(con, SongID, PlaylistID).access().getOutputParam();
            if (obj != null) {
                LOGGER.info("Song successfully deleted from playlist.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Song-playlist not found. Cannot delete it.");

                m = new Message("Song-playlist not found. Cannot delete it.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the song: wrong format for URI /songs/{id}.", ex);

            m = new Message("Cannot delete the song: wrong format for URI /songs/{id}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the song from playlist: other resources depend on it.");

                m = new Message("Cannot delete the song from playlist: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the song from playlist: unexpected database error.", ex);

                m = new Message("Cannot delete the song from playlist: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}