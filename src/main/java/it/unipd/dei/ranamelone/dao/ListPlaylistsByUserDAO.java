package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Playlists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListPlaylistsByUserDAO extends AbstractDAO<List<Playlists>> {
    private static final String STATEMENT = "SELECT * FROM Playlists WHERE UserID = ?";
    private final int id;

    /**
     * Creates a new DAO object for showing all Playlists made by User.
     *
     * @param con the connection to be used for accessing the database.
     */
    public ListPlaylistsByUserDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    public final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Playlists> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                objs.add(new Playlists(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getInt("userID")
                ));
            }
            LOGGER.info("Operation SUCCESS");
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = objs;
    }
}