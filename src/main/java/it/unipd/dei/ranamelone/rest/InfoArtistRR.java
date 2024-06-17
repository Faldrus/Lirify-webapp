package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.InfoArtistDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Artists;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InfoArtistRR extends AbstractRR {
    /**
     * Creates a new REST resource for info about Artist.
     * > GET rest/artists/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public InfoArtistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.INFO_ARTIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Artists obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int ArtistID = Integer.parseInt(path.substring(path.lastIndexOf("artists") + 8));

            obj = new InfoArtistDAO(con, ArtistID).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("Artist successfully read.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Artist not found. Cannot read it.");

                m = new Message(String.format("Artist %d not found. Cannot read it.", ArtistID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot read the artist: wrong format for URI /artists/{ID}.", ex);

            m = new Message("Cannot read the artist: wrong format for URI /artists/{ID}.", "E4A7",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot read the artist: unexpected database error.", ex);

            m = new Message("Cannot read the artist: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}