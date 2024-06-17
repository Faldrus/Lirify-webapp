package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeSongDAO extends AbstractDAO<Boolean> {
    private static final String STATEMENT = "INSERT INTO LikedSongs (UserID, SongID) VALUES (?, ?) RETURNING *";
    private final int userId;
    private final int songId;

    /**
     * Creates a new DAO object for liking Song.
     *
     * @param con the connection to be used for accessing the database.
     */
    public LikeSongDAO(final Connection con, final int userId, final int songId) {
        super(con);
        this.userId = userId;
        this.songId = songId;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean result = false;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.userId);
            pstmt.setInt(2, this.songId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = true;
                LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = result;
    }
}