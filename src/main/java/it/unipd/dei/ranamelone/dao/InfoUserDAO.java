package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoUserDAO extends AbstractDAO<Users> {
    private static final String STATEMENT = "SELECT ID, Username, Email, Password, Avatar FROM Users WHERE ID = ?";
    private final int id;

    /**
     * Creates a new DAO object for showing info about User.
     *
     * @param con the connection to be used for accessing the database.
     */
    public InfoUserDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Users obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
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
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = obj;
    }
}