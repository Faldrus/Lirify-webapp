package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.InfoAlbumDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InfoAlbumRR extends AbstractRR {
    /**
     * Creates a new REST resource for info about Album.
     * > GET rest/albums/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public InfoAlbumRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.INFO_ALBUM, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Albums obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int AlbumID = Integer.parseInt(path.substring(path.lastIndexOf("albums") + 7));

            obj = new InfoAlbumDAO(con, AlbumID).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("Album successfully read.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Album not found. Cannot read it.");

                m = new Message(String.format("Album %d not found. Cannot read it.", AlbumID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot read the album: wrong format for URI /albums/{ID}.", ex);

            m = new Message("Cannot read the album: wrong format for URI /albums/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot read the album: unexpected database error.", ex);

            m = new Message("Cannot read the album: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}