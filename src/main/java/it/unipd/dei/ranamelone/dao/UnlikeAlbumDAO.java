package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnlikeAlbumDAO extends AbstractDAO<Boolean> {
    private static final String STATEMENT = "DELETE FROM LikedAlbums WHERE UserID = ? AND AlbumID = ? RETURNING *";
    private final int idUser;
    private final int idAlbum;

    /**
     * Creates a new DAO object for deleting LikedAlbum.
     *
     * @param con the connection to be used for accessing the database.
     */
    public UnlikeAlbumDAO(final Connection con, final int idUser, final int idAlbum) {
        super(con);
        this.idUser = idUser;
        this.idAlbum = idAlbum;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean result = false;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.idUser);
            pstmt.setInt(2, this.idAlbum);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = true;
                LOGGER.info("Operation SUCCESS");
            }
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = result;
    }
}