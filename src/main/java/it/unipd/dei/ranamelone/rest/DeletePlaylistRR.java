package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.DeletePlaylistDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.Playlists;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public final class DeletePlaylistRR extends AbstractRR{
    /**
     * Creates a new REST resource for deleting Playlist.
     * > DELETE rest/playlists/{ID}
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeletePlaylistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_PLAYLIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Playlists obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int PlaylistID = Integer.parseInt(path.substring(path.lastIndexOf("playlists") + 10));

            obj = new DeletePlaylistDAO(con, PlaylistID).access().getOutputParam();

            if(obj != null) {
                LOGGER.info("Playlist successfully deleted.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Playlist not found. Cannot delete it.");

                m = new Message(String.format("Playlist %d not found. Cannot delete it.", PlaylistID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the playlist: wrong format for URI /playlists/{id}.", ex);

            m = new Message("Cannot delete the playlists: wrong format for URI /playlists/{id}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the playlist: other resources depend on it.");

                m = new Message("Cannot delete the playlist: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the playlist: unexpected database error.", ex);

                m = new Message("Cannot delete the playlist: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}