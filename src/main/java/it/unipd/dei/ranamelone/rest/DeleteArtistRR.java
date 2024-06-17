package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.DeleteArtistDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.Artists;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteArtistRR extends AbstractRR {
    /**
     * Creates a new REST resource for deleting Artist.
     * > DELETE rest/artists/{ID}
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteArtistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_ARTIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Artists obj  = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int ArtistID = Integer.parseInt(path.substring(path.lastIndexOf("artists") + 8));

            obj = new DeleteArtistDAO(con, ArtistID).access().getOutputParam();

            if(obj != null) {
                LOGGER.info("Artist successfully deleted.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Artist not found. Cannot delete it.");

                m = new Message(String.format("Artist %d not found. Cannot delete it.", ArtistID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the artist: wrong format for URI /artists/{id}.", ex);

            m = new Message("Cannot delete the artist: wrong format for URI /artists/{id}.", "E4A7", ex.getMessage());
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