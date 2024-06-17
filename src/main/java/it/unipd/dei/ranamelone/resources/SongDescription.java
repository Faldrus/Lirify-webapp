package it.unipd.dei.ranamelone.resources;

public class SongDescription extends AbstractResource {
    private final Songs song;
    private final Artists artist;

    public SongDescription(Songs song, Artists artist) {
        this.song = song;
        this.artist = artist;
    }

    public SongDescription() { this(null, null); }

    public final Artists getArtist() { return artist; }
    public final Songs getSong() { return song; }
}
