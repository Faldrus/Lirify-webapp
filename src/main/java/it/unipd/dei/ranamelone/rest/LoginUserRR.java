package it.unipd.dei.ranamelone.rest;

import it.unipd.dei.ranamelone.dao.LoginUserDAO;
import it.unipd.dei.ranamelone.resources.Actions;
import it.unipd.dei.ranamelone.resources.Users;
import it.unipd.dei.ranamelone.resources.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginUserRR extends AbstractRR {
    /**
     * Creates a new REST resource for User Login.
     * > POST rest/users/login
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public LoginUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LOGIN, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Users obj = null;
        Message m = null;

        try {
            final Users user = Users.fromJSON(req.getInputStream());

            if (user.getEmail().isBlank() || user.getPassword().isBlank()) {
                LOGGER.warn("Cannot login: email or password is blank");

                m = new Message("Cannot login: email or password is blank", "E4A8", "Cannot login: email or password is blank");
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
                return;
            }

            obj = new LoginUserDAO(con, user).access().getOutputParam();

            if (obj != null) {
                LOGGER.info("User successfully logged in.");

                res.setStatus(HttpServletResponse.SC_OK);
                obj.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("User not found. Cannot log in.");

                m = new Message("User not found. Cannot log in.", "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot log in the user: wrong format.", ex);

            m = new Message("Cannot log in the user: wrong format.", "E4A7", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot log in the users: unexpected database error.", ex);

            m = new Message("Cannot log in the users: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}