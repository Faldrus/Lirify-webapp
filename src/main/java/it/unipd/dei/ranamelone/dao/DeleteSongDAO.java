package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteSongDAO extends AbstractDAO<Songs> {
    private static final String STATEMENT  = "DELETE FROM Songs WHERE ID = ? RETURNING *";
    private static final String STATEMENT2 = "DELETE FROM LikedSongs WHERE SongID = ? RETURNING *";
    private static final String STATEMENT3 = "DELETE FROM SongsInPlaylists WHERE SongID = ? RETURNING *";
    private static final String STATEMENT4 = "DELETE FROM SongsInAlbums WHERE SongID = ? RETURNING *";
    private final int id;

    /**
     * Creates a new DAO object for deleting Song.
     *
     * @param con the connection to be used for accessing the database.
     */
     public DeleteSongDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try { // delete from SongsInAlbums
            pstmt = con.prepareStatement(STATEMENT4);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from SongsInPlaylists
            pstmt = con.prepareStatement(STATEMENT3);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from LikedSongs
            pstmt = con.prepareStatement(STATEMENT2);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        Songs obj = null;
        try { // delete from Songs
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Songs(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Youtube_Link"),
                        rs.getDate("Release_Date"),
                        rs.getString("Lyrics"),
                        rs.getString("Genre"),
                        rs.getString("Duration"),
                        rs.getInt("ArtistID")
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