package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Playlists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreatePlaylistDAO extends AbstractDAO<Playlists> {
    private static final String STATEMENT = "INSERT INTO Playlists (ID, Title, UserID) VALUES (DEFAULT, ?, ?) RETURNING *";
    private final Playlists obj;

    /**
     * Creates a new DAO object for creating Playlist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public CreatePlaylistDAO(final Connection con, final Playlists obj) {
        super(con);
        if (obj == null) {
            LOGGER.error("The object cannot be null.");
            throw new NullPointerException("The object cannot be null.");
        }
        this.obj = obj;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Playlists obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.obj.getTitle());
            pstmt.setInt(2, this.obj.getUserId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Playlists(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getInt("UserID")
                );
                LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = obj;
    }
}