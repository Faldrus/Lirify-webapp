package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.UnlikeAlbumDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UnlikeAlbumRR extends AbstractRR {
    /**
     * Creates a new REST resource for User unliking Album.
     * > DELETE rest/users/{ID}/albums/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public UnlikeAlbumRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.UNLIKE_ALBUM, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        boolean result = false;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int UserID = Integer.parseInt(path.substring(path.lastIndexOf("users") + 6, path.lastIndexOf("albums") - 1));
            final int AlbumID = Integer.parseInt(path.substring(path.lastIndexOf("albums") + 7));

            result = new UnlikeAlbumDAO(con, UserID, AlbumID).access().getOutputParam();

            if (result) {
                LOGGER.info("Album successfully unliked.");

                res.setStatus(HttpServletResponse.SC_OK);
            } else {
                LOGGER.warn("Album unliking failed.");

                m = new Message("Cannot unlike the album: unexpected error.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot unlike the album: wrong format for URI /users/{ID}/albums/{ID}.", ex);

            m = new Message("Cannot unlike the album: wrong format for URI /users/{ID}/albums/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot unlike the album: other resources depend on it.");

                m = new Message("Cannot unlike the album: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot unlike the album: unexpected database error.", ex);

                m = new Message("Cannot unlike the album: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}