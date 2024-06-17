package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Albums extends AbstractResource {
    private final int id;
    private final String title;
    private final String coverImage;
    private final Date releaseDate;
    private final String genre;
    private final int artistId;

    public Albums(final int id, final String title, final String coverImage, final Date releaseDate, final String genre, final int artistId) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.artistId = artistId;
    }

    public Albums() {
        this(-1, null, null, null, null, -1);
    }

    public final int getId() { return id; }
    public final String getTitle() { return title; }
    public final String getCoverImage() { return coverImage; }
    public final Date getReleaseDate() { return releaseDate; }
    public final String getGenre() { return genre; }
    public final int getArtistId() { return artistId; }

    public static Albums fromJSON(final InputStream in) throws IOException {
        try {
            return new ObjectMapper().enable(DeserializationFeature.UNWRAP_ROOT_VALUE).readerFor(Albums.class).readValue(in);
        } catch (IOException e) { throw new IOException("Error while reading JSON data from input stream", e); }
    }
}