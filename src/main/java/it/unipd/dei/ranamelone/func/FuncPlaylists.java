package it.unipd.dei.ranamelone.func;

import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.Playlists;
import it.unipd.dei.ranamelone.rest.CreatePlaylistRR;
import it.unipd.dei.ranamelone.rest.ListPlaylistsByUserRR;
import it.unipd.dei.ranamelone.rest.DeletePlaylistRR;
import it.unipd.dei.ranamelone.rest.InfoPlaylistRR;
import it.unipd.dei.ranamelone.servlet.AbstractDatabaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.sql.Connection;
import java.util.Objects;

/**
 * Functions for {@link Playlists}
 */
public class FuncPlaylists {
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDatabaseServlet.class, StringFormatterMessageFactory.INSTANCE);
    public static boolean processPlaylists(final HttpServletRequest req, final HttpServletResponse res, final Connection con) throws Exception {
        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;
        if (path.lastIndexOf("rest/playlists") <= 0) { return false; }                        // Skip to next request type in the dispatcher
        path = path.substring(path.lastIndexOf("playlists") + 9);                   // Strip URL

        // Pattern matching...
        if (path.matches("^/?$") && Objects.equals(method, "POST")) {                    // POST   rest/playlists
            new CreatePlaylistRR(req, res, con).serve();
        } else if (path.matches("^/users/(\\d+)/?$") && Objects.equals(method, "GET")) { // GET    rest/playlists/users/{ID}
            new ListPlaylistsByUserRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "DELETE")) {    // DELETE rest/playlists/{ID}
            new DeletePlaylistRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "GET")) {       // GET    rest/playlists/{ID}
            new InfoPlaylistRR(req, res, con).serve();
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