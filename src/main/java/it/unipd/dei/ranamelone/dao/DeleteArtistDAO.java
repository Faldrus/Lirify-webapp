package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Artists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteArtistDAO extends AbstractDAO<Artists> {
    private static final String STATEMENT   = "DELETE FROM Artists WHERE ID = ? RETURNING *";
    private static final String STATEMENT2  = "DELETE FROM Songs WHERE ArtistID = ? RETURNING *";
    private static final String STATEMENT2A = "DELETE FROM LikedSongs WHERE SongID IN ";
    private static final String STATEMENT2B = "DELETE FROM SongsInPlaylists WHERE SongID IN ";
    private static final String STATEMENT3  = "DELETE FROM Albums WHERE ArtistID = ? RETURNING *";
    private static final String STATEMENT3A = "DELETE FROM LikedAlbums WHERE AlbumID IN ";
    private static final String STATEMENT3B = "DELETE FROM SongsInAlbums WHERE AlbumID IN ";
    private final int id;

    /**
     * Creates a new DAO object for deleting Artist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public DeleteArtistDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Integer> deletedAlbumsIds = new ArrayList<>();
        List<Integer> deletedSongsIds = new ArrayList<>();

        //
        // ALBUMS DELETION
        //
        try { // delete from Albums
            pstmt = con.prepareStatement(STATEMENT3);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                deletedAlbumsIds.add(rs.getInt("ID"));
            }
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from LikedAlbums
            StringBuilder sb = new StringBuilder(STATEMENT3A);
            sb.append("(");
            for (int i = 0; i < deletedAlbumsIds.size(); i++) {
                if (i > 0) { sb.append(","); }
                sb.append(deletedAlbumsIds.get(i));
            }
            sb.append(")");
            pstmt = con.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from SongsInAlbums
            StringBuilder sb = new StringBuilder(STATEMENT3B);
            sb.append("(");
            for (int i = 0; i < deletedAlbumsIds.size(); i++) {
                if (i > 0) { sb.append(","); }
                sb.append(deletedAlbumsIds.get(i));
            }
            sb.append(")");
            pstmt = con.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        //
        // SONGS DELETION
        //
        try { // delete from Songs
            pstmt = con.prepareStatement(STATEMENT2);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                deletedSongsIds.add(rs.getInt("ID"));
            }
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from LikedSongs
            StringBuilder sb = new StringBuilder(STATEMENT2A);
            sb.append("(");
            for (int i = 0; i < deletedSongsIds.size(); i++) {
                if (i > 0) { sb.append(","); }
                sb.append(deletedSongsIds.get(i));
            }
            sb.append(")");
            pstmt = con.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from SongsInPlaylists
            StringBuilder sb = new StringBuilder(STATEMENT2B);
            sb.append("(");
            for (int i = 0; i < deletedSongsIds.size(); i++) {
                if (i > 0) { sb.append(","); }
                sb.append(deletedSongsIds.get(i));
            }
            sb.append(")");
            pstmt = con.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        //
        // ARTISTS DELETION
        //
        pstmt = null;
        rs = null;
        Artists obj = null;
        try { // delete from Artists
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