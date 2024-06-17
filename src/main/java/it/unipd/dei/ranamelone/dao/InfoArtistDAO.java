package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Artists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoArtistDAO extends AbstractDAO<Artists> {
    private static final String STATEMENT = "SELECT ID, Name, Email, Password, Artist_Image, Biography, Verified, Language FROM Artists WHERE ID = ?";
    private final int id;

    /**
     * Creates a new DAO object for showing info about Artist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public InfoArtistDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Artists obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
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
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = obj;
    }
}