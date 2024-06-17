package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Artists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginArtistDAO extends AbstractDAO<Artists> {
    private static final String STATEMENT = "SELECT ID, Name, Email, Password, Artist_Image, Biography, Verified, Language FROM Artists WHERE Email = ? AND Password = ?";
    private final Artists obj;

    /**
     * Creates a new DAO object for Artist Login.
     *
     * @param con the connection to be used for accessing the database.
     */
    public LoginArtistDAO(final Connection con, final Artists obj) {
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

        Artists obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.obj.getEmail());
            pstmt.setString(2, this.obj.getPassword());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Artists(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        "DONT_PEEK_:)",
                        rs.getString("Artist_Image"),
                        rs.getString("Biography"),
                        rs.getBoolean("Verified"),
                        rs.getString("Language")
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