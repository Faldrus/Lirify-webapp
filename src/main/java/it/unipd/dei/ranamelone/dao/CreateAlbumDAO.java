package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateAlbumDAO extends AbstractDAO<Albums> {
    private static final String STATEMENT = "INSERT INTO Albums (ID, Title, Album_Cover_Image, Release_Date, Genre, ArtistID) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING *";
    private final Albums obj;

    /**
     * Creates a new DAO object for creating Album.
     *
     * @param con the connection to be used for accessing the database.
     */
    public CreateAlbumDAO(final Connection con, final Albums obj) {
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

        Albums obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.obj.getTitle());
            pstmt.setString(2, this.obj.getCoverImage());
            pstmt.setDate(3, new java.sql.Date(this.obj.getReleaseDate().getTime()));
            pstmt.setString(4, this.obj.getGenre());
            pstmt.setInt(5, this.obj.getArtistId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Albums(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Album_Cover_Image"),
                        rs.getDate("Release_Date"),
                        rs.getString("Genre"),
                        rs.getInt("ArtistID")
                );
                LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = obj;
    }
}