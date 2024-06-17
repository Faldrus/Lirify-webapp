package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.ranamelone.resources.Artists;
import it.unipd.dei.ranamelone.resources.Albums;
import it.unipd.dei.ranamelone.resources.Songs;
import java.util.List;

public class SearchList extends AbstractResource{
    private final List<Artists> artists;
    private final List<Albums> albums;
    private final List<Songs> songs;

    public SearchList(List<Artists> artists, List<Albums> albums, List<Songs> songs) {
        this.artists = artists;
        this.albums = albums;
        this.songs = songs;
    }

    public List<Artists> getArtists() {return artists;}
    public List<Albums> getAlbums() {return albums;}
    public List<Songs> getSongs() {return songs;}
}
