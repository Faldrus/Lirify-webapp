package it.unipd.dei.ranamelone.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public enum ErrorCode {

    WRONG_FORMAT(-100, HttpServletResponse.SC_BAD_REQUEST,"Wrong format."),
    EMPTY_INPUT_FIELDS(-101, HttpServletResponse.SC_BAD_REQUEST, "One or more input fields are empty."),
    EMAIL_MISSING(-202, HttpServletResponse.SC_BAD_REQUEST, "Email missing"),
    PASSWORD_MISSING(-104, HttpServletResponse.SC_BAD_REQUEST, "Password missing"),
    WRONG_CREDENTIALS(-105, HttpServletResponse.SC_BAD_REQUEST, "Submitted credentials are wrong"),
    WRONG_INTERVALS(-107, HttpServletResponse.SC_CONFLICT, "Different passwords introduced"),
    DUPLICATE_EMAIL(-108, HttpServletResponse.SC_CONFLICT, "Email is already in use"),
    DUPLICATE_USERNAME(-209, HttpServletResponse.SC_BAD_REQUEST, "Username already exists"),

    CREATEPLAYLIST_ERROR(-116, HttpServletResponse.SC_BAD_REQUEST, "Problems in creating playlist"),
    CREATEPLALBUM_ERROR(-117, HttpServletResponse.SC_BAD_REQUEST, "Problems in creating album"),
    CREATEARTIST_ERROR(-118, HttpServletResponse.SC_BAD_REQUEST, "Problems in creating artist"),
    CREATESONG_ERROR(-119, HttpServletResponse.SC_BAD_REQUEST, "Problems in creating song"),
    CREATEUSER_ERROR(-120, HttpServletResponse.SC_BAD_REQUEST, "Problems in creating user"),


    UPDATEUSER_ERROR(-301, HttpServletResponse.SC_BAD_REQUEST, "Problems in updating user"),
    UPDATEARTIST_ERROR(-302, HttpServletResponse.SC_BAD_REQUEST, "Problems in updating artist"),
    LYRICS_UPLOAD_FAILED(-303, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to upload lyrics"),

    SONG_DOESNOTEXIST(-112, HttpServletResponse.SC_BAD_REQUEST, "Song not found"),
    LIKE_FAILED_SONG(-410, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to like the song"),
    SONG_ALREADY_LIKED(-411, HttpServletResponse.SC_BAD_REQUEST, "Song already liked by the user"),

    ALBUM_DOESNOTEXIST(-113, HttpServletResponse.SC_BAD_REQUEST, "Album not found"),
    LIKE_FAILED_ALBUM(-412, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to like the album"),
    ALBUM_ALREADY_LIKED(-413, HttpServletResponse.SC_BAD_REQUEST, "Album already liked by the user"),


    SONG_ID_INVALID(-600, HttpServletResponse.SC_BAD_REQUEST, "Invalid Song ID"),
    SONG_TITLE_MISSING(-601, HttpServletResponse.SC_BAD_REQUEST, "Song title missing"),
    YOUTUBE_URL_INVALID(-602, HttpServletResponse.SC_BAD_REQUEST, "Invalid YouTube URL"),
    RELEASE_DATE_INVALID(-603, HttpServletResponse.SC_BAD_REQUEST, "Invalid release date for the song"),
    DURATION_INVALID(-604, HttpServletResponse.SC_BAD_REQUEST, "Invalid duration for the song"),

    ARTIST_ID_INVALID(-700, HttpServletResponse.SC_BAD_REQUEST, "Invalid Artist ID"),
    ARTIST_NAME_MISSING(-701, HttpServletResponse.SC_BAD_REQUEST, "Artist name missing"),
    BIOGRAPHY_MISSING(-702, HttpServletResponse.SC_BAD_REQUEST, "Biography missing for the artist"),
    VERIFICATION_MISSING(-703, HttpServletResponse.SC_BAD_REQUEST, "Verification status missing for the artist"),
    LANGUAGE_MISSING(-704, HttpServletResponse.SC_BAD_REQUEST, "Language missing for the artist"),

    ALBUM_ID_INVALID(-800, HttpServletResponse.SC_BAD_REQUEST, "Invalid Album ID"),
    ALBUM_TITLE_MISSING(-801, HttpServletResponse.SC_BAD_REQUEST, "Album title missing"),
    ALBUM_IMAGE_MISSING(-802, HttpServletResponse.SC_BAD_REQUEST, "Album image missing"),
    ALBUM_RELEASE_DATE_INVALID(-803, HttpServletResponse.SC_BAD_REQUEST, "Invalid release date for the album"),

    PLAYLIST_ID_INVALID(-900, HttpServletResponse.SC_BAD_REQUEST, "Invalid Playlist ID"),
    PLAYLIST_TITLE_MISSING(-901, HttpServletResponse.SC_BAD_REQUEST, "Playlist title missing"),

    USER_ID_INVALID(-1000, HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID"),
    USERNAME_MISSING(-1001, HttpServletResponse.SC_BAD_REQUEST, "Username missing"),
    AVATAR_MISSING(-1004, HttpServletResponse.SC_BAD_REQUEST, "Avatar missing"),
    AVATAR_INVALID_FORMAT(-1005, HttpServletResponse.SC_BAD_REQUEST, "Invalid avatar format"),

    ALBUM_NOTFOUND(-114, HttpServletResponse.SC_BAD_REQUEST, "Album not found"),
    ARTIST_NOTFOUND(-115, HttpServletResponse.SC_BAD_REQUEST, "Artist not found"),
    BADLY_FORMATTED_JSON(-121,  HttpServletResponse.SC_BAD_REQUEST, "The input json is in the wrong format."),
    DIFFERENT_PASSWORDS(-130,HttpServletResponse.SC_BAD_REQUEST,"The passwords are not the same"),
    INVALID_REQUEST(-400, HttpServletResponse.SC_BAD_REQUEST, "Invalid request"),
    ACCESS_DENIED(-403, HttpServletResponse.SC_FORBIDDEN, "Access denied"),
    UNAUTHORIZED(-401, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access"),
    DATABASE_ERROR(-500, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error"),
    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");



    private final int errorCode;
    private final int httpCode;
    private final String errorMessage;

    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getHTTPCode() {
        return httpCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("code", errorCode);
        data.put("message", errorMessage);
        JSONObject info = new JSONObject();
        info.put("error", data);
        return info;
    }


}