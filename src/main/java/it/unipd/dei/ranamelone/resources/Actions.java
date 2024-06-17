package it.unipd.dei.ranamelone.resources;

public final class Actions {
    public static final String CREATE_USER = "CREATE_USER";
    public static final String CREATE_ARTIST = "CREATE_ARTIST";
    public static final String CREATE_PLAYLIST = "CREATE_PLAYLIST";
    public static final String CREATE_ALBUM = "CREATE_ALBUM";
    public static final String CREATE_SONG = "CREATE_SONG";
    public static final String DELETE_USER = "DELETE_USER";
    public static final String DELETE_SONG = "DELETE_SONG";
    public static final String DELETE_ALBUM = "DELETE_ALBUM";
    public static final String DELETE_ARTIST = "DELETE_ARTIST";
    public static final String DELETE_PLAYLIST = "DELETE_PLAYLIST";
    public static final String ADD_SONGS_TO_ALBUM = "ADD_SONGS_TO_ALBUM";
    public static final String ADD_SONGS_TO_PLAYLIST = "ADD_SONGS_TO_PLAYLIST";
    public static final String DELETE_SONG_FROM_PLAYLIST = "DELETE_SONG_FROM_PLAYLIST";
    public static final String INFO_ALBUM = "INFO_ALBUM";
    public static final String INFO_ARTIST = "INFO_ARTIST";
    public static final String INFO_PLAYLIST = "INFO_PLAYLIST";
    public static final String INFO_SONG = "INFO_SONG";
    public static final String INFO_USER = "INFO_USER";
    public static final String LIST_ALBUMS_BY_ALGORITHM = "LIST_ALBUMS_BY_ALGORITHM";
    public static final String LIST_SONGS_BY_ALGORITHM = "LIST_SONGS_BY_ALGORITHM";
    public static final String UNLIKE_ALBUM = "UNLIKE_ALBUM";
    public static final String UNLIKE_SONG = "UNLIKE_SONG";
    public static final String UPDATE_USER = "UPDATE_USER";
    public static final String UPDATE_ARTIST = "UPDATE_ARTIST";
    public static final String LIKE_ALBUM = "LIKE_ALBUM";
    public static final String LIKE_SONG = "LIKE_SONG";
    public static final String LIST_ALBUMS_BY_ARTIST = "LIST_ALBUMS_BY_ARTIST";
    public static final String LIST_ALBUMS_LIKED_BY_USER = "LIST_ALBUMS_LIKED_BY_USER";
    public static final String LIST_PLAYLIST_BY_USER = "LIST_PLAYLIST_BY_USER";
    public static final String LIST_SONGS_BY_ARTIST = "LIST_SONGS_BY_ARTIST";
    public static final String LIST_SONGS_BY_ALBUM = "LIST_SONGS_BY_ALBUM";
    public static final String LIST_SONGS_BY_PLAYLIST = "LIST_SONGS_BY_PLAYLIST";
    public static final String LIST_SONGS_LIKED_BY_USER = "LIST_SONGS_LIKED_BY_USER";
    public static final String LOGIN = "LOGIN";
    public static final String SEARCH = "SEARCH";

    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}
