package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListAlbumsLikedByUserDAO extends AbstractDAO<List<Albums>> {
    private static final String STATEMENT = "SELECT Albums.* FROM LikedAlbums JOIN Albums ON LikedAlbums.AlbumID = Albums.ID WHERE LikedAlbums.UserID = ?";
    private final int id;

    /**
     * Creates a new DAO object for showing all Albums liked by User.
     *
     * @param con the connection to be used for accessing the database.
     */
    public ListAlbumsLikedByUserDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Albums> objs = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.id);
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
            LOGGER.info("Operation SUCCESS");
        } finally {
            if (rs != null) { rs.close(); }
            if (pstmt != null) { pstmt.close(); }
        }
        outputParam = objs;
    }
}