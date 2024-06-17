package it.unipd.dei.ranamelone.func;

import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.Songs;
import it.unipd.dei.ranamelone.rest.AddSongsToAlbumRR;
import it.unipd.dei.ranamelone.rest.ListSongsInAlbumRR;
import it.unipd.dei.ranamelone.rest.AddSongsToPlaylistRR;
import it.unipd.dei.ranamelone.rest.DeleteSongFromPlaylistRR;
import it.unipd.dei.ranamelone.rest.ListSongsInPlaylistRR;
import it.unipd.dei.ranamelone.rest.CreateSongRR;
import it.unipd.dei.ranamelone.rest.ListSongsByArtistRR;
import it.unipd.dei.ranamelone.rest.ListSongsByAlgorithmRR;
import it.unipd.dei.ranamelone.rest.DeleteSongRR;
import it.unipd.dei.ranamelone.rest.InfoSongRR;
import it.unipd.dei.ranamelone.rest.SearchSongsRR;
import it.unipd.dei.ranamelone.servlet.AbstractDatabaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.sql.Connection;
import java.util.Objects;

/**
 * Functions for {@link Songs}
 */
public class FuncSongs {
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDatabaseServlet.class, StringFormatterMessageFactory.INSTANCE);
    public static boolean processSongs(final HttpServletRequest req, final HttpServletResponse res, final Connection con) throws Exception {
        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;
        if (path.lastIndexOf("rest/songs") <= 0) { return false; }                                          // Skip to next request type in the dispatcher
        path = path.substring(path.lastIndexOf("songs") + 5);                                     // Strip URL

        // Pattern matching...
        if (path.matches("^/?$") && Objects.equals(method, "POST")) {                                  // POST   rest/songs
            new CreateSongRR(req, res, con).serve();
        } else if (path.matches("^/artists/(\\d+)/?$") && Objects.equals(method, "GET")) {             // GET    rest/songs/artists/{ID}
            new ListSongsByArtistRR(req, res, con).serve();
        } else if (path.matches("^/pickalgorithm/(\\d+)/?$") && Objects.equals(method, "GET")) {       // GET    rest/songs/pickalgorithm/{NUM}
            new ListSongsByAlgorithmRR(req, res, con).serve();
        } else if (path.matches("^/albums/(\\d+)/?$") && Objects.equals(method, "GET")) {              // GET    rest/songs/albums/{ID}
            new ListSongsInAlbumRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/albums/(\\d+)/?$") && Objects.equals(method, "POST")) {      // POST   rest/songs/{ID}/albums/{ID}
            new AddSongsToAlbumRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/playlists/(\\d+)/?$") && Objects.equals(method, "DELETE")) { // DELETE rest/songs/{ID}/playlists/{ID}
            new DeleteSongFromPlaylistRR(req, res, con).serve();
        } else if (path.matches("^/playlists/(\\d+)/?$") && Objects.equals(method, "GET")) {           // GET    rest/songs/playlists/{ID}
            new ListSongsInPlaylistRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/playlists/(\\d+)/?$") && Objects.equals(method, "POST")) {   // POST   rest/songs/{ID}/playlists/{ID}
            new AddSongsToPlaylistRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "DELETE")) {                  // DELETE rest/songs/{ID}
            new DeleteSongRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "GET")) {                     // GET    rest/songs/{ID}
            new InfoSongRR(req, res, con).serve();
        } else if (path.matches("^/search/.*") && Objects.equals(method, "GET")) {                     // GET    rest/songs/search/...
            new SearchSongsRR(req, res, con).serve();
        } else {
            LOGGER.warn(String.format("Method %s not supported", method));

            m = new Message("Unsupported operation", "E4A5", String.format("Requested operation %s.", method));
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            m.toJSON(res.getOutputStream());
            return false;
        }
        return true;
    }
}