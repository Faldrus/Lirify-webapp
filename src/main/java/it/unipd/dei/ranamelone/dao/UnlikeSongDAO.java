package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnlikeSongDAO extends AbstractDAO<Boolean>{
    private static final String STATEMENT = "DELETE FROM LikedSongs WHERE UserID = ? AND SongID = ? RETURNING *";
    private final int idUser;
    private final int idSong;

    /**
     * Creates a new DAO object for deleting LikedSongs.
     *
     * @param con the connection to be used for accessing the database.
     */
    public UnlikeSongDAO(final Connection con, final int idUser, final int idSong) {
        super(con);
        this.idUser = idUser;
        this.idSong = idSong;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean result = false;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.idUser);
            pstmt.setInt(2, this.idSong);
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