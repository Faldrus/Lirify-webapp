package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.ListSongsByAlgorithmDAO;
import it.unipd.dei.ranamelone.resources.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListSongsByAlgorithmRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing Songs by algorithm (< 1yr old).
     * > GET rest/songs/pickalgorithm/{NUM}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public ListSongsByAlgorithmRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_SONGS_BY_ALGORITHM, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<SongDescription> objs = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int NumAlg = Integer.parseInt(path.substring(path.lastIndexOf("pickalgorithm") + 14));

            objs = new ListSongsByAlgorithmDAO(con, NumAlg).access().getOutputParam();

            if (objs != null) {
                LOGGER.info("Song(s) successfully read.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(objs).toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("No song(s) found. Cannot read them.");

                m = new Message("Song(s) not found. Cannot read them.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot read the song(s): unexpected database error.", ex);

            m = new Message("Cannot read the song(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}