package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUserDAO extends AbstractDAO<Users> {
    private static final String STATEMENT = "SELECT ID, Username, Email, Password, Avatar FROM Users WHERE Email = ? AND Password = ?";
    private final Users obj;

    /**
     * Creates a new DAO object for User Login.
     *
     * @param con the connection to be used for accessing the database.
     */
    public LoginUserDAO(final Connection con, final Users obj) {
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
            pstmt.setString(1, this.obj.getEmail());
            pstmt.setString(2, this.obj.getPassword());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Users(
                        rs.getInt("ID"),
                        rs.getString("Email"),
                        rs.getString("Username"),
                        "DONT_PEEK_:)",
                        rs.getString("Avatar")
                );
                LOGGER.info("Operation SUCCESS");
            }
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = obj;
    }
}