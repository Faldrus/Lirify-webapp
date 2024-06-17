package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.AddSongsToAlbumDAO;
import it.unipd.dei.ranamelone.dao.CreateAlbumWithSongsDAO;
import it.unipd.dei.ranamelone.resources.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreateAlbumWithSongsRR extends AbstractRR {
    /**
     * Creates a new REST resource for adding Song to Album.
     * > POST rest/songs/albums/{ID}
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreateAlbumWithSongsRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.ADD_SONGS_TO_ALBUM, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Albums result = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int AlbumID = Integer.parseInt(path.substring(path.lastIndexOf("albums") + 7));

            final AlbumWithSongs albumWithSongs = AlbumWithSongs.fromJSON(req.getInputStream());

            Albums albums = albumWithSongs.getAlbums();
            ArrayList<Songs> songsInAlbum = albumWithSongs.getSongs();

            result = new CreateAlbumWithSongsDAO(con, albums, songsInAlbum).access().getOutputParam();

            if (result != null) {
                LOGGER.info("Song successfully added to album.");

                res.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                LOGGER.error("Fatal error while adding song to album.");

                m = new Message("Cannot add song to album: unexpected error.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (NumberFormatException ex) {
            LOGGER.warn("Cannot add song to album: invalid album ID or song ID.", ex);

            m = new Message("Cannot add song to album: invalid album ID or song ID.", "E4A6", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot add song to album: song already exists in album.");

                m = new Message("Cannot add song to album: song already exists in album.", "E5A2", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot add song to album: unexpected database error.", ex);

                m = new Message("Cannot add song to album: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}