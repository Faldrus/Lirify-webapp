package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteAlbumDAO extends AbstractDAO<Albums> {
    private static final String STATEMENT  = "DELETE FROM Albums WHERE ID = ? RETURNING *";
    private static final String STATEMENT2 = "DELETE FROM LikedAlbums WHERE AlbumID = ? RETURNING *";
    private static final String STATEMENT3 = "DELETE FROM SongsInAlbums WHERE AlbumID = ? RETURNING *";
    private final int id;

    /**
     * Creates a new DAO object for deleting Album.
     *
     * @param con the connection to be used for accessing the database.
     */
    public DeleteAlbumDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try { // delete from SongsInAlbums
            pstmt = con.prepareStatement(STATEMENT3);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from LikedAlbums
            pstmt = con.prepareStatement(STATEMENT2);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        Albums obj = null;
        try { // delete from Albums
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Albums(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Album_Cover_Image"),
                        rs.getDate("Release_Date"),
                        rs.getString("Genre"),
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