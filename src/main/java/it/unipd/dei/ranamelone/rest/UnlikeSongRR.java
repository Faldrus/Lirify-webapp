package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.UnlikeSongDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UnlikeSongRR extends AbstractRR {
    /**
     * Creates a new REST resource for User unliking Song.
     * > DELETE rest/users/{ID}/songs/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public UnlikeSongRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.UNLIKE_SONG, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        boolean result = false;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int UserID = Integer.parseInt(path.substring(path.lastIndexOf("users") + 6, path.lastIndexOf("songs") - 1));
            final int SongID = Integer.parseInt(path.substring(path.lastIndexOf("songs") + 6));

            result = new UnlikeSongDAO(con, UserID, SongID).access().getOutputParam();

            if (result) {
                LOGGER.info("Song successfully unliked.");

                res.setStatus(HttpServletResponse.SC_OK);
            } else {
                LOGGER.warn("Song unliking failed.");

                m = new Message("Cannot unlike the song: unexpected error.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot unlike the song: wrong format for URI /users/{ID}/songs/{ID}.", ex);

            m = new Message("Cannot unlike the song: wrong format for URI /users/{ID}/songs/{ID}.", "E4A7",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot unlike the song: other resources depend on it.");

                m = new Message("Cannot unlike the song: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot unlike the song: unexpected database error.", ex);

                m = new Message("Cannot unlike the song: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}