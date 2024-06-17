package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.UpdateArtistDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Artists;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateArtistRR extends AbstractRR {
    /**
     * Creates a new REST resource for updating Artist.
     * > PUT rest/artists/{ID}
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public UpdateArtistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.UPDATE_ARTIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Artists obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("artists") + 7);
            final int ArtistID = Integer.parseInt(path.substring(1));

            final Artists artist = Artists.fromJSON(req.getInputStream());
            if (ArtistID != artist.getId()) {
                LOGGER.warn(String.format("Cannot update the artist: URI request (%d) and artist resource (%d) ID differ.", ArtistID, artist.getId()));

                m = new Message("Cannot update the artist: URI request and artist resource ID differ.", "E4A8", String.format("Request URI ID %d; artist resource ID %d.", ArtistID, artist.getId()));
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
                return;
            }

            obj = new UpdateArtistDAO(con, artist).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("Artist successfully updated.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Artist not found. Cannot update it.");

                m = new Message(String.format("Artist %d not found. Cannot update it.", ArtistID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the artists: wrong format for URI /artists/{id}.", ex);

            m = new Message("Cannot delete the artist: wrong format for URI /artists/{id}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (EOFException ex) {
            LOGGER.warn("Cannot update the artist: no artist JSON object found in the request.", ex);

            m = new Message("Cannot update the artist: no artist JSON object found in the request.", "E4A8", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the artist: other resources depend on it.");

                m = new Message("Cannot delete the artist: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the artist: unexpected database error.", ex);

                m = new Message("Cannot delete the artist: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}