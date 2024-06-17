package it.unipd.dei.ranamelone.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddSongsToPlaylistDAO extends AbstractDAO<Boolean> {
    private static final String STATEMENT = "INSERT INTO SongsInPlaylists (SongID, PlaylistID) VALUES (?, ?) RETURNING *";
    private final int playlistId;
    private final int songId;

    /**
     * Creates a new DAO object for adding Song to Playlist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public AddSongsToPlaylistDAO(final Connection con, final int playlistId, final int songId) {
        super(con);
        this.playlistId = playlistId;
        this.songId = songId;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean result = false;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.songId);
            pstmt.setInt(2, this.playlistId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = true;
                LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = result;
    }
}