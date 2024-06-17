package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.LikeAlbumDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LikeAlbumRR extends AbstractRR {
    /**
     * Creates a new REST resource for User liking Album.
     * > POST rest/users/{ID}/albums/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public LikeAlbumRR(final HttpServletRequest req, final HttpServletResponse res, final Connection con) {
        super(Actions.LIKE_ALBUM, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        boolean result = false;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int UserID = Integer.parseInt(path.substring(path.lastIndexOf("users") + 6, path.lastIndexOf("albums") - 1));
            final int AlbumID = Integer.parseInt(path.substring(path.lastIndexOf("albums") + 7));

            result = new LikeAlbumDAO(con, UserID, AlbumID).access().getOutputParam();

            if (result) {
                LOGGER.info("Album successfully liked.");

                res.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                LOGGER.error("Album liking failed.");

                m = new Message("Cannot like the album: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (IOException ex) {
            LOGGER.warn("Cannot like the album: no User or Album JSON object found in the request.", ex);

            m = new Message("Cannot like the album: no User or Album JSON object found in the request.", "E4A8", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot like the album: unexpected database error.", ex);

            m = new Message("Cannot like the album: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}