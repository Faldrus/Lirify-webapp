package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.InfoSongDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import it.unipd.dei.ranamelone.resources.Songs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InfoSongRR extends AbstractRR {
    /**
     * Creates a new REST resource for info about Song.
     * > GET rest/songs/{ID}
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public InfoSongRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.INFO_SONG, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Songs obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int SongID = Integer.parseInt(path.substring(path.lastIndexOf("songs") + 6));

            obj = new InfoSongDAO(con, SongID).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("Song successfully read.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Song not found. Cannot read it.");

                m = new Message(String.format("Song %d not found. Cannot read it.", SongID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot read the song: wrong format for URI /songs/{ID}.", ex);

            m = new Message("Cannot read the song: wrong format for URI /songs/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot read the song: unexpected database error.", ex);

            m = new Message("Cannot read the song: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}