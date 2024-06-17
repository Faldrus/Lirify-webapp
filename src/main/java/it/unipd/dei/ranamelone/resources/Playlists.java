package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Playlists extends AbstractResource {
    private final int id;
    private final String title;
    private final int userId;

    public Playlists(final int id, final String title, final int userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
    }

    public Playlists() {
        this(-1, null, -1);
    }

    public final int getId() { return id; }
    public final String getTitle() { return title; }
    public final int getUserId() { return userId; }

    public static Playlists fromJSON(final InputStream in) throws IOException {
        try {
            return new ObjectMapper().enable(DeserializationFeature.UNWRAP_ROOT_VALUE).readerFor(Playlists.class).readValue(in);
        } catch (IOException e) { throw new IOException("Error while reading JSON data from input stream", e); }
    }
}