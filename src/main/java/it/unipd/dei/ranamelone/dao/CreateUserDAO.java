package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateUserDAO extends AbstractDAO<Users> {
    private static final String STATEMENT = "INSERT INTO users (ID, Username, Email, Password, Avatar) VALUES (DEFAULT, ?, ?, ?, ?) RETURNING *";
    private final Users obj;

    /**
     * Creates a new DAO object for creating User.
     *
     * @param con the connection to be used for accessing the database.
     */
    public CreateUserDAO(final Connection con, final Users obj) {
        super(con);
        if (obj == null) {
            LOGGER.error("The object cannot be null.");
            throw new NullPointerException("The object cannot be null.");
        }
        this.obj = obj;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Users obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.obj.getUsername());
            pstmt.setString(2, this.obj.getEmail());
            pstmt.setString(3, this.obj.getPassword());
            pstmt.setString(4, this.obj.getAvatar());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                 obj = new Users(
                         rs.getInt("ID"),
                         rs.getString("Username"),
                         rs.getString("Email"),
                         rs.getString("Password"),
                         rs.getString("Avatar")
                 );
                 LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = obj;
    }
}