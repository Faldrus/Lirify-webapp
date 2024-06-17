package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.SearchAlbumsDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.ResourceList;
import it.unipd.dei.ranamelone.resources.Albums;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SearchAlbumsRR extends AbstractRR {
    /**
     * Creates a new REST resource for searching.
     * > GET rest/albums/search/{string}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public SearchAlbumsRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.SEARCH, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Albums> objs = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final String search = path.substring(path.indexOf("search") + 7);

            objs = new SearchAlbumsDAO(con, search).access().getOutputParam();

            if (objs != null) {
                LOGGER.info("Search successfully completed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(objs).toJSON(res.getOutputStream());

            } else {
                LOGGER.warn("Search not found. Cannot read it.");

                m = new Message(String.format("Search %s not found. Cannot read it.", search), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot search: wrong format for URI /search/{string}.", ex);

            m = new Message("Cannot search: wrong format for URI /search/{string}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot search: unexpected database error.", ex);

            m = new Message("Cannot search: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}