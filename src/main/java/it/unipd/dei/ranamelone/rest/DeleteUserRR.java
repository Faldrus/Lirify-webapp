package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.DeleteUserDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Users;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteUserRR extends AbstractRR {
    /**
     * Creates a new REST resource for deleting User.
     * > DELETE rest/users/{ID}
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Users obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            final int UserID = Integer.parseInt(path.substring(path.lastIndexOf("users") + 6));

            obj = new DeleteUserDAO(con, UserID).access().getOutputParam();

            if(obj != null) {
                LOGGER.info("User successfully deleted.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("User not found. Cannot delete it.");

                m = new Message(String.format("User %d not found. Cannot delete it.", UserID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the user: wrong format for URI /users/{id}.", ex);

            m = new Message("Cannot delete the user: wrong format for URI /users/{id}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the users: other resources depend on it.");

                m = new Message("Cannot delete the users: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the user: unexpected database error.", ex);

                m = new Message("Cannot delete the user: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}