package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Playlists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteSongFromPlaylistDAO extends AbstractDAO<Playlists> {
    private static final String STATEMENT = "DELETE FROM SongsInPlaylists WHERE SongID = ? AND PlaylistID = ? RETURNING *";
    private final int idSong;
    private final int idPlaylist;

    /**
     * Creates a new DAO object for deleting Song from Playlist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public DeleteSongFromPlaylistDAO(final Connection con, final int idSong, final int idPlaylist) {
        super(con);
        this.idSong = idSong;
        this.idPlaylist = idPlaylist;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Playlists obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.idSong);
            pstmt.setInt(2, this.idPlaylist);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Playlists(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getInt("UserID")
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