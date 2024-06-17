package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListSongsByArtistDAO extends AbstractDAO<List<Songs>> {
    private static final String STATEMENT = "SELECT * FROM Songs WHERE ArtistID = ?";
    private final int id;

    /**
     * Creates a new DAO object for showing all Songs made by Artist.
     *
     * @param con the connection to be used for accessing the database.
     */
    public ListSongsByArtistDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Songs> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                objs.add(new Songs(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Youtube_Link"),
                        rs.getDate("Release_Date"),
                        rs.getString("Lyrics"),
                        rs.getString("Genre"),
                        rs.getString("Duration"),
                        rs.getInt("ArtistID")
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