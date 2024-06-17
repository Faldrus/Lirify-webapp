package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.CreateAlbumDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CreateAlbumRR extends AbstractRR {
    /**
     * Creates a new REST resource for creating Album.
     * > POST rest/albums
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreateAlbumRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CREATE_ALBUM, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Albums obj = null;
        Message m = null;

        try {
            final Albums album = Albums.fromJSON(req.getInputStream());

            obj = new CreateAlbumDAO(con, album).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("Album successfully created.");

                res.setStatus(HttpServletResponse.SC_CREATED);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Album creation failed.");

                m = new Message("Cannot create the album: unexpected error.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot create the album: no album JSON object found in the request.", ex);

            m = new Message("Cannot create the album: no album JSON object found in the request.", "E4A8", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create the album: it already exists.");

                m = new Message("Cannot create the album: it already exists.", "E5A2", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create the album: unexpected database error.", ex);

                m = new Message("Cannot create the album: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}