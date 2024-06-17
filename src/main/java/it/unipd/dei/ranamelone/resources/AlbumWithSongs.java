package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AlbumWithSongs extends AbstractResource{
    private final Albums albums;
    private final ArrayList<Songs> songs;

    public AlbumWithSongs(Albums albums, ArrayList<Songs> songs) {
        this.albums = albums;
        this.songs = songs;
    }

    public Albums getAlbums() {
        return albums;
    }

    public ArrayList<Songs> getSongs() {
        return songs;
    }

    public static AlbumWithSongs fromJSON(final InputStream in) throws IOException {
        try {
            return new ObjectMapper().enable(DeserializationFeature.UNWRAP_ROOT_VALUE).readerFor(AlbumWithSongs.class).readValue(in);
        } catch (IOException e) { throw new IOException("Error while reading JSON data from input stream", e); }
    }
}
