package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.ListAlbumsByArtistDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.ResourceList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListAlbumsByArtistRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing Albums by Artist.
     * > GET rest/albums/artists/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public ListAlbumsByArtistRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_ALBUMS_BY_ARTIST, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Albums> objs = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("artists") + 7);
            final int ArtistID = Integer.parseInt(path.substring(1));

            objs = new ListAlbumsByArtistDAO(con, ArtistID).access().getOutputParam();

            if (objs != null) {
                LOGGER.info(String.format("Album(s) successfully searched by artist with id %d.", ArtistID));

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(objs).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("No album(s) found. Cannot read them.");

                m = new Message("No album(s) found. Cannot read them.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot search album(s): wrong format for URI /albums/artists/{ID}.", ex);

            m = new Message("Cannot search album(s): wrong format for URI /albums/artists/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot search album(s): unexpected database error.", ex);

            m = new Message("Cannot search album(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}