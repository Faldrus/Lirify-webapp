package it.unipd.dei.ranamelone.func;

import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.Artists;
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
 * Functions for {@link Artists}
 */
public class FuncArtists {
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDatabaseServlet.class, StringFormatterMessageFactory.INSTANCE);
    public static boolean processArtists(final HttpServletRequest req, final HttpServletResponse res, final Connection con) throws Exception {
        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;
        if (path.lastIndexOf("rest/artists") <= 0) { return false; }                       // Skip to next request type in the dispatcher
        path = path.substring(path.lastIndexOf("artists") + 7);                  // Strip URL

        // Patter matching...
        if (path.matches("^/?$") && Objects.equals(method, "POST")) {                 // POST   rest/artists
            new CreateArtistRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "DELETE")) { // DELETE rest/artists/{ID}
            new DeleteArtistRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "GET")) {    // GET    rest/artists/{ID}
            new InfoArtistRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "PUT")) {    // PUT    rest/artists/{ID}
            new UpdateArtistRR(req, res, con).serve();
        } else if (path.matches("^/login/?$") && Objects.equals(method, "POST")) {    // POST   rest/artists/login
            new LoginArtistRR(req, res, con).serve();
        } else if (path.matches("^/search/.*") && Objects.equals(method, "GET")) {    // GET    rest/artists/search/...
            new SearchArtistsRR(req, res, con).serve();
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