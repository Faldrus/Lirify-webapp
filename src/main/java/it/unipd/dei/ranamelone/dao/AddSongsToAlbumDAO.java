package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddSongsToAlbumDAO extends AbstractDAO<Boolean> {
    private static final String STATEMENT = "INSERT INTO SongsInAlbums (SongID, AlbumID) VALUES (?, ?) RETURNING *";
    private final int albumId;
    private final int songId;

    /**
     * Creates a new DAO object for adding Song to Album.
     *
     * @param con the connection to be used for accessing the database.
     */
    public AddSongsToAlbumDAO(final Connection con, final int albumId, final int songId) {
        super(con);
        this.albumId = albumId;
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