package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Songs;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CreateSongDAO extends AbstractDAO<Songs> {
    private static final String STATEMENT = "INSERT INTO Songs (ID, Title, Youtube_Link, Release_Date, Lyrics, Genre, Duration, ArtistID) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING *";
    private final Songs obj;

    /**
     * Creates a new DAO object for creating Song.
     *
     * @param con the connection to be used for accessing the database.
     */
    public CreateSongDAO(final Connection con, final Songs obj) {
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

        Songs obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.obj.getTitle());
            pstmt.setString(2, this.obj.getYoutubeLink());
            pstmt.setDate(3, new java.sql.Date(this.obj.getReleaseDate().getTime()));
            pstmt.setString(4, this.obj.getLyrics());
            pstmt.setString(5, this.obj.getGenre());
            pstmt.setTime(6, fromStringToTime(this.obj.getDuration()));
            pstmt.setInt(7, this.obj.getArtistId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = new Songs(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Youtube_Link"),
                        rs.getDate("Release_Date"),
                        rs.getString("Lyrics"),
                        rs.getString("Genre"),
                        rs.getString("Duration"),
                        rs.getInt("ArtistID")
                );
                LOGGER.info("Operation SUCCESS");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally { if (pstmt != null) { pstmt.close(); }}
        outputParam = obj;
    }

    Time fromStringToTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long ms = sdf.parse(time).getTime();
        return new java.sql.Time(ms);
    }
}