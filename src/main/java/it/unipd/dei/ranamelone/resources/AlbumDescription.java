package it.unipd.dei.ranamelone.resources;

public class AlbumDescription extends AbstractResource {
    private final Albums album;
    private final Artists artist;

    public AlbumDescription(Albums album, Artists artist) {
        this.album = album;
        this.artist = artist;
    }

    public AlbumDescription() { this(null, null); }

    public final Artists getArtist() { return artist; }
    public final Albums getAlbum() { return album; }
}
