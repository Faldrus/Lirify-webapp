package it.unipd.dei.ranamelone.func;

import it.unipd.dei.ranamelone.resources.Message;
import it.unipd.dei.ranamelone.resources.Users;
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
 * Functions for {@link Users}
 */
public class FuncUsers {
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDatabaseServlet.class, StringFormatterMessageFactory.INSTANCE);
    public static boolean processUsers(final HttpServletRequest req, final HttpServletResponse res, final Connection con) throws Exception {
        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;
        if (path.lastIndexOf("rest/users") <= 0) {
            return false;
        }                                       // Skip to next request type in the dispatcher
        path = path.substring(path.lastIndexOf("users") + 5);                                  // Strip URL

        // Pattern matching...
        if (path.matches("^/?$") && Objects.equals(method, "POST")) {                               // POST   rest/users
            new CreateUserRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/albums/(\\d+)/?$") && Objects.equals(method, "DELETE")) { // DELETE rest/users/{ID}/albums/{ID}
            new UnlikeAlbumRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/albums/?$") && Objects.equals(method, "GET")) {           // GET    rest/users/{ID}/albums
            new ListAlbumsLikedByUserRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/albums/(\\d+)/?$") && Objects.equals(method, "POST")) {   // POST   rest/users/{ID}/albums/{ID}
            new LikeAlbumRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/songs/(\\d+)/?$") && Objects.equals(method, "DELETE")) {  // DELETE rest/users/{ID}/songs/{ID}
            new UnlikeSongRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/songs/?$") && Objects.equals(method, "GET")) {            // GET    rest/users/{ID}/songs
            new ListSongsLikedByUserRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/songs/(\\d+)/?$") && Objects.equals(method, "POST")) {    // POST   rest/users/{ID}/songs/{ID}
            new LikeSongRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "DELETE")) {               // DELETE rest/users/{ID}
            new DeleteUserRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "GET")) {                  // GET    rest/users/{ID}
            new InfoUserRR(req, res, con).serve();
        } else if (path.matches("^/(\\d+)/?$") && Objects.equals(method, "PUT")) {                  // PUT    rest/users/{ID}
            new UpdateUserRR(req, res, con).serve();
        } else if (path.matches("^/login/?$") && Objects.equals(method, "POST")) {                  // POST   rest/users/login
            new LoginUserRR(req, res, con).serve();
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