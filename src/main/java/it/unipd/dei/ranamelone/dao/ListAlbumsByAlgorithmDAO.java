package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.AlbumDescription;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.Artists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListAlbumsByAlgorithmDAO extends AbstractDAO<List<AlbumDescription>> {
    private static final String STATEMENT = "SELECT Albums.ID AS A_ID, Albums.*, Artists.* FROM Albums JOIN Artists ON Albums.ArtistID = Artists.ID WHERE Albums.Release_Date >= NOW() - INTERVAL '1 year' ORDER BY RANDOM() LIMIT ?";
    private final int num;

    /**
     * Creates a new DAO object for showing all Albums not older than 1 year.
     *
     * @param con the connection to be used for accessing the database.
     */
    public ListAlbumsByAlgorithmDAO(final Connection con, final int num) {
        super(con);
        this.num = num;
    }

    @Override
    public final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<AlbumDescription> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.num);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                objs.add(new AlbumDescription(new Albums(
                        rs.getInt("A_ID"),
                        rs.getString("Title"),
                        rs.getString("Album_Cover_Image"),
                        rs.getDate("Release_Date"),
                        rs.getString("Genre"),
                        rs.getInt("ArtistID")
                ), new Artists(
                        rs.getInt("ArtistID"),
                        rs.getString("Name"),
                        "DONT_PEEK_:)",
                        "DONT_PEEK_:)",
                        rs.getString("Artist_Image"),
                        rs.getString("Biography"),
                        rs.getBoolean("Verified"),
                        rs.getString("Language")
                )));
            }
            LOGGER.info("Operation SUCCESS");
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = objs;
    }
}