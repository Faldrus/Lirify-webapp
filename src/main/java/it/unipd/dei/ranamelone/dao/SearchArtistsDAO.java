package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Artists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchArtistsDAO extends AbstractDAO<List<Artists>> {
    private static final String STATEMENT = "SELECT * FROM Artists WHERE Name ~* ?";
    private final String search;

    /**
     * Creates a new DAO object for searching.
     *
     * @param con the connection to be used for accessing the database.
     */
    public SearchArtistsDAO(final Connection con, final String search) {
        super(con);
        this.search = search;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Artists> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.search);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                objs.add(new Artists(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        "DONT_PEEK_:)",
                        rs.getString("Artist_Image"),
                        rs.getString("Biography"),
                        rs.getBoolean("Verified"),
                        rs.getString("Language")
                ));
            }
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = objs;
    }
}