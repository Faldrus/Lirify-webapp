package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchSongsDAO extends AbstractDAO<List<Songs>> {
    private static final String STATEMENT = "SELECT * FROM Songs WHERE Title ~* ?";
    private final String search;

    /**
     * Creates a new DAO object for searching.
     *
     * @param con the connection to be used for accessing the database.
     */
    public SearchSongsDAO(final Connection con, final String search) {
        super(con);
        this.search = search;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Songs> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.search);
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
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = objs;
    }
}