package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Artists extends AbstractResource {
    private final Integer id;
    private final String name;
    private final String email;
    private final String password;
    private final String image;
    private final String biography;
    private final boolean verified;
    private final String language;

    public Artists(final Integer id, final String name, final String email, final String password, final String image, final String biography, final boolean verified, final String language) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.biography = biography;
        this.verified = verified;
        this.language = language;
    }

    public Artists() {
        this(null, null, null, null, null, null, false, null);
    }

    public final Integer getId() { return id; }
    public final String getName() { return name; }
    public final String getEmail() { return email; }
    public final String getPassword() { return password; }
    public final String getImage() { return image; }
    public final String getBiography() { return biography; }
    public final boolean isVerified() { return verified; }
    public final String getLanguage() { return language; }

    public static Artists fromJSON(final InputStream in) throws IOException {
        try {
            return new ObjectMapper().enable(DeserializationFeature.UNWRAP_ROOT_VALUE).readerFor(Artists.class).readValue(in);
        } catch (IOException e) { throw new IOException("Error while reading JSON data from input stream", e); }
    }
}