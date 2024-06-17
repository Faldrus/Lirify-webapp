package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Users extends AbstractResource {
    private final Integer id;
    private final String username;
    private final String email;
    private final String password;
    private final String avatar;

    public Users(final Integer id, final String username, final String email, final String password, final String avatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public Users() {
        this(null, null, null, null, null);
    }

    public final Integer getId() { return id; }
    public final String getUsername() { return username; }
    public final String getEmail() { return email; }
    public final String getPassword() { return password; }
    public final String getAvatar() { return avatar; }

    public static Users fromJSON(final InputStream in) throws IOException {
        try {
            return new ObjectMapper().enable(DeserializationFeature.UNWRAP_ROOT_VALUE).readerFor(Users.class).readValue(in);
        } catch (IOException e) { throw new IOException("Error while reading JSON data from input stream", e); }
    }
}