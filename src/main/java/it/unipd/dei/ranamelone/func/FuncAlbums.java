package it.unipd.dei.ranamelone.func;

import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.rest.*;
import it.unipd.dei.ranamelone.servlet.AbstractDatabaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.sql.Connection;
import java.util.Objects;

/**
 * Functions for {@link Albums}
 */
public class FuncAlbums {
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDatabaseServlet.class, StringFormatterMessageFactory.INSTANCE);
    public static boolean processAlbums(final HttpServletRequest req, final HttpServletResponse res, final Connection con) throws Exception {
        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;
        if (path.lastIndexOf("rest/albums") <= 0) { return false; }                                    // Skip to next request type in the dispatcher
        path = path.substring(path.lastIndexOf("albums") + 6);                               // Strip URL

        // Pattern matching...
        if (path.matches("^/?$") && Objects.equals(method, "POST")) {                             // POST   rest/albums
            new CreateAlbumRR(req, res, con).serve();
        } else if (path.matches("^/artists/(\\d+)/?$") && Objects.equals(method, "GET")) {        // GET    rest/albums/artists/{ID}
            new ListAlbumsByArtistRR(req, res, con).serve();
        } else if (path.matches("^/pickalgorithm/(\\d+)/?$") && Objects.equals(method, "GET")) {  // GET    rest/albums/pickalgorithm/{NUM}
            new ListAlbumsByAlgorithmRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "DELETE")) {             // DELETE rest/albums/{ID}
            new DeleteAlbumRR(req, res, con).serve();
        }  else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "POST")) {              // POST rest/albums/{ID}
            new CreateAlbumWithSongsRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "GET")) {                // GET    rest/albums/{ID}
            new InfoAlbumRR(req, res, con).serve();
        } else if (path.matches("^/search/.*") && Objects.equals(method, "GET")) {                // GET    rest/albums/search/...
            new SearchAlbumsRR(req, res, con).serve();
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