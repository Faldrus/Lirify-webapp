package it.unipd.dei.ranamelone.servlet;

import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

import static it.unipd.dei.ranamelone.func.FuncAlbums.processAlbums;
import static it.unipd.dei.ranamelone.func.FuncArtists.processArtists;
import static it.unipd.dei.ranamelone.func.FuncPlaylists.processPlaylists;
import static it.unipd.dei.ranamelone.func.FuncSongs.processSongs;
import static it.unipd.dei.ranamelone.func.FuncUsers.processUsers;

/**
 * Dispatches the request to the proper REST resource.
 */
public final class RestDispatcherServlet extends AbstractDatabaseServlet {
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        final OutputStream out = res.getOutputStream();

        try {
            // add /rest/{call(s)} cases with if(s): --> Func{NAME} --> {NAME}RR --> {DatabaseAction}DAO (uses WriteJSON function in /resources)
            final Connection con = getConnection();
            if (processAlbums           (req, res, con)) { return; }
            if (processArtists          (req, res, con)) { return; }
            if (processPlaylists        (req, res, con)) { return; }
            if (processSongs            (req, res, con)) { return; }
            if (processUsers            (req, res, con)) { return; }

            // if none of the above process methods succeeds, it means an unknown resource has been requested
            LOGGER.warn("Unknown resource requested: %s.", req.getRequestURI());

            final Message m = new Message(
                    "Unknown resource requested.",
                    "E4A6",
                    String.format("Requested resource is %s.", req.getRequestURI())
            );
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.setContentType(JSON_UTF_8_MEDIA_TYPE);
            m.toJSON(out);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error while processing the REST resource.", t);
            final Message m = new Message("Unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(out);
        } finally {
            // ensure to always flush and close the output stream
            if (out != null) {
                out.flush();
                out.close();
            }
            LogContext.removeIPAddress();
        }
    }
}