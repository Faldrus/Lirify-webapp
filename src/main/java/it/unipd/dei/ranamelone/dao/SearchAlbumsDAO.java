package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchAlbumsDAO extends AbstractDAO<List<Albums>> {
    private static final String STATEMENT = "SELECT * FROM Albums WHERE Title ~* ?";
    private final String search;

    /**
     * Creates a new DAO object for searching.
     *
     * @param con the connection to be used for accessing the database.
     */
    public SearchAlbumsDAO(final Connection con, final String search) {
        super(con);
        this.search = search;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Albums> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.search);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                objs.add(new Albums(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Album_Cover_Image"),
                        rs.getDate("Release_Date"),
                        rs.getString("Genre"),
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