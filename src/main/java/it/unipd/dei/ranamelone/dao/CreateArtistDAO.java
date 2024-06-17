package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Artists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateArtistDAO extends AbstractDAO<Artists> {
    private static final String STATEMENT = "INSERT INTO Artists (ID, Name, Email, Password, Artist_Image, Biography, Verified, Language) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING *";
    private final Artists obj;

    /**
     * Creates a new DAO object for creating Artist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public CreateArtistDAO(final Connection con, final Artists obj) {
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
            pstmt.setString(1, this.obj.getName());
            pstmt.setString(2, this.obj.getEmail());
            pstmt.setString(3, this.obj.getPassword());
            pstmt.setString(4, this.obj.getImage());
            pstmt.setString(5, this.obj.getBiography());
            pstmt.setBoolean(6, this.obj.isVerified());
            pstmt.setString(7, this.obj.getLanguage());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Artists(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("Artist_Image"),
                        rs.getString("Biography"),
                        rs.getBoolean("Verified"),
                        rs.getString("Language")
                );
                LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = obj;
    }
}