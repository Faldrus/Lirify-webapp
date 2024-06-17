package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Songs extends AbstractResource {
    private final int id;
    private final String title;
    private final String youtubeLink;
    private final Date releaseDate;
    private final String lyrics;
    private final String genre;
    private final String duration;
    private final int artistId;

    public Songs(final int id, final String title, final String youtubeLink, final Date releaseDate, final String lyrics, final String genre, final String duration, final int artistId) {
        this.id = id;
        this.title = title;
        this.youtubeLink = youtubeLink;
        this.releaseDate = releaseDate;
        this.lyrics = lyrics;
        this.genre = genre;
        this.duration = duration;
        this.artistId = artistId;
    }

    public Songs() {
        this(-1, null, null, null, null, null, null, 0);
    }

    public final int getId() { return id; }
    public final String getTitle() { return title; }
    public final String getYoutubeLink() { return youtubeLink; }
    public final Date getReleaseDate() { return releaseDate; }
    public final String getLyrics() { return lyrics; }
    public final String getGenre() { return genre; }
    public final String getDuration() { return duration; }
    public final int getArtistId() { return artistId; }

    public static Songs fromJSON(final InputStream in) throws IOException {
        try {
            return new ObjectMapper().enable(DeserializationFeature.UNWRAP_ROOT_VALUE).readerFor(Songs.class).readValue(in);
        } catch (IOException e) { throw new IOException("Error while reading JSON data from input stream", e); }
    }
}