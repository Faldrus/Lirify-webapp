package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteUserDAO extends AbstractDAO<Users> {
    private static final String STATEMENT   = "DELETE FROM Users WHERE ID = ? RETURNING *";
    private static final String STATEMENT2  = "DELETE FROM LikedSongs WHERE UserID = ? RETURNING *";
    private static final String STATEMENT3  = "DELETE FROM LikedAlbums WHERE UserID = ? RETURNING *";
    private static final String STATEMENT4A = "DELETE FROM Playlists WHERE UserID = ? RETURNING *";
    private static final String STATEMENT4B = "DELETE FROM SongsInPlaylists WHERE PlaylistID IN ";
    private final int id;

    /**
     * Creates a new DAO object for deleting User.
     *
     * @param con the connection to be used for accessing the database.
     */
    public DeleteUserDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Integer> deletedPlaylistsIds = new ArrayList<>();

        //
        // LIKES
        //
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
        try { // delete from LikedAlbums
            pstmt = con.prepareStatement(STATEMENT3);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        //
        // PLAYLISTS
        //
        pstmt = null;
        rs = null;
        try { // delete from Pleylists
            pstmt = con.prepareStatement(STATEMENT4A);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                deletedPlaylistsIds.add(rs.getInt("ID"));
            }
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        pstmt = null;
        rs = null;
        try { // delete from SongsInPlaylists
            StringBuilder sb = new StringBuilder(STATEMENT4B);
            sb.append("(");
            for (int i = 0; i < deletedPlaylistsIds.size(); i++) {
                if (i > 0) { sb.append(","); }
                sb.append(deletedPlaylistsIds.get(i));
            }
            sb.append(")");
            pstmt = con.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }

        //
        // USERS
        //
        pstmt = null;
        rs = null;
        Users obj = null;
        try { // delete from Users
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Users(
                        rs.getInt("ID"),
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("Avatar")
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