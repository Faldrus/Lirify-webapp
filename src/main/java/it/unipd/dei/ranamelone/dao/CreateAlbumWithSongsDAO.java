package it.unipd.dei.ranamelone.dao;

import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.Artists;
import it.unipd.dei.ranamelone.resources.Songs;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

public class CreateAlbumWithSongsDAO extends AbstractDAO<Albums> {
    // Create Album
    private static final String STATEMENT1 = "INSERT INTO Albums (Title, Album_Cover_Image, Release_Date, Genre, ArtistID) VALUES (?, ?, ?, ?, ?) RETURNING AlbumID";
    // Create songs
    private static String STATEMENT2 = "INSERT INTO Songs (Title, Youtube_Link, Release_Date, Genre, Duration, ArtistID) VALUES "; //sistemare
    // Connect songs to album
    private static String STATEMENT3 = "INSERT INTO SongsInAlbums (SongID, AlbumID) VALUES ";

    private final Albums albums;
    private final ArrayList<Songs> songs;

    public CreateAlbumWithSongsDAO(final Connection con, final Albums albums, final ArrayList<Songs> songs) {
        super(con);

        this.albums = albums;
        this.songs = songs;
    }

    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Integer obj = null;
        try {
            pstmt = con.prepareStatement(STATEMENT1);
            pstmt.setString(1, this.albums.getTitle());
            pstmt.setString(2, this.albums.getCoverImage());
            pstmt.setDate(3, new java.sql.Date(this.albums.getReleaseDate().getTime()));
            pstmt.setString(4, this.albums.getGenre());
            pstmt.setInt(5, this.albums.getArtistId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                obj = rs.getInt("ID");
                LOGGER.info("Operation SUCCESS");
            }
        } finally { if (pstmt != null) { pstmt.close(); }}

        PreparedStatement pstmt2 = null;
        ResultSet rs2 = null;

        ArrayList<Integer> obj2 = new ArrayList<>();

        try {
            String query = STATEMENT2;
            for(int i = 0; i < songs.size() - 1; i++){
                query = query.concat("(?, ?, ?, ?, ?, ?, ?), ");
            }
            query = query.concat("(?, ?, ?, ?, ?, ?, ?) RETURNING *");

            int songCounter = 0;
            pstmt2 = con.prepareStatement(STATEMENT2);
            for(Songs song : songs){
                pstmt2.setString(1+7*songCounter, song.getTitle());
                pstmt2.setString(2+7*songCounter, song.getYoutubeLink());
                pstmt2.setDate(3+7*songCounter, new java.sql.Date(song.getReleaseDate().getTime()));
                pstmt2.setString(4+7*songCounter, song.getLyrics());
                pstmt2.setString(5+7*songCounter, song.getGenre());
                pstmt2.setString(6+7*songCounter, song.getDuration());
                pstmt2.setInt(7+7*songCounter, song.getArtistId());

                songCounter++;
            }
            rs2 = pstmt2.executeQuery();
            while(rs2.next()) {
                obj2.add(rs2.getInt("ID"));
            }
        } finally { if (pstmt2 != null) { pstmt2.close(); }}

        PreparedStatement pstmt3 = null;
        ResultSet rs3 = null;


        try {
            String query = STATEMENT3;
            for(int i = 0; i < songs.size() - 1; i++) {
                query = query.concat("(?, ?), ");
            }
            query = query.concat("(?, ?) RETURNING *");

            int songCounter = 0;
            for(Integer songID : obj2) {
                pstmt3 = con.prepareStatement(STATEMENT3);
                pstmt3.setInt(1+2*songCounter, songID);
                pstmt3.setInt(2+2*songCounter, obj);

                songCounter++;
            }

            rs3 = pstmt3.executeQuery();



        } finally { if (pstmt3 != null) {pstmt3.close(); }}
    }
}
