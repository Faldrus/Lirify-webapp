package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.UpdateUserDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Users;
import it.unipd.dei.ranamelone.resources.LogContext;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateUserRR extends AbstractRR {
    /**
     * Creates a new REST resource for updating User.
     * > PUT rest/users/{ID}
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public UpdateUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.UPDATE_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Users obj = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("users") + 5);
            final int UserID = Integer.parseInt(path.substring(1));

            final Users user = Users.fromJSON(req.getInputStream());
            if (UserID != user.getId()) {
                LOGGER.warn(String.format("Cannot update the user: URI request (%d) and user resource (%d) badges differ.", UserID, user.getId()));

                m = new Message("Cannot update the user: URI request and user resource badges differ.", "E4A8", String.format("Request URI badge %d; user resource badge %d.", UserID, user.getId()));
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
                return;
            }

            obj = new UpdateUserDAO(con, user).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("User successfully updated.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("User not found. Cannot update it.");

                m = new Message(String.format("Users %d not found. Cannot update it.", UserID), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the user: wrong format for URI /users/{ID}.", ex);

            m = new Message("Cannot delete the user: wrong format for URI /users/{ID}.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (EOFException ex) {
            LOGGER.warn("Cannot updated the user: no user JSON object found in the request.", ex);

            m = new Message("Cannot update the user: no user JSON object found in the request.", "E4A8", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the user: other resources depend on it.");

                m = new Message("Cannot delete the user: other resources depend on it.", "E5A4", ex.getMessage());
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