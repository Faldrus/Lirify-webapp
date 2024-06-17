package it.unipd.dei.ranamelone.resources;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a resource able to serialize itself to JSON.
 */
public interface Resource {
    void toJSON(final OutputStream out) throws IOException;
}
