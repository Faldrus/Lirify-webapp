package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Playlists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoPlaylistDAO extends AbstractDAO<Playlists> {
    private static final String STATEMENT = "SELECT ID, Title, UserID FROM Playlists WHERE ID = ?";
    private final int id;

    /**
     * Creates a new DAO object for showing info about Playlist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public InfoPlaylistDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Playlists obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
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