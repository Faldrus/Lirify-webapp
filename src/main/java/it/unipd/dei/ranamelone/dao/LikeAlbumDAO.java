package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeAlbumDAO extends AbstractDAO<Boolean> {
    private static final String STATEMENT = "INSERT INTO LikedAlbums (UserID, AlbumID) VALUES (?, ?) RETURNING *";
    private final int userId;
    private final int albumId;

    /**
     * Creates a new DAO object for liking Album.
     *
     * @param con the connection to be used for accessing the database.
     */
    public LikeAlbumDAO(final Connection con, final int userId, final int albumId) {
        super(con);
        this.userId = userId;
        this.albumId = albumId;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean result = false;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.userId);
            pstmt.setInt(2, this.albumId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = true;
                LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = result;
    }
}