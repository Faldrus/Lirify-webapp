package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Artists;
import it.unipd.dei.ranamelone.resources.SongDescription;
import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListSongsByAlgorithmDAO extends AbstractDAO<List<SongDescription>> {
    private static final String STATEMENT = "SELECT Songs.ID AS S_ID, Songs.*, Artists.* FROM Songs JOIN Artists ON Songs.ArtistID = Artists.ID WHERE Songs.Release_Date >= NOW() - INTERVAL '1 year' ORDER BY RANDOM() LIMIT ?";
    private final int num;

    /**
     * Creates a new DAO object for showing all Songs not older than 1 year.
     *
     * @param con the connection to be used for accessing the database.
     */
    public ListSongsByAlgorithmDAO(final Connection con, final int num) {
        super(con);
        this.num = num;
    }

    @Override
    public final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<SongDescription> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.num);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                objs.add(new SongDescription(new Songs(
                        rs.getInt("S_ID"),
                        rs.getString("Title"),
                        rs.getString("Youtube_Link"),
                        rs.getDate("Release_Date"),
                        rs.getString("Lyrics"),
                        rs.getString("Genre"),
                        rs.getString("Duration"),
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