package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Provides a base implementation for {@code Resource} classes.
 */
public abstract class AbstractResource implements Resource {
    protected static final Logger LOGGER = LogManager.getLogger(
            AbstractResource.class,
            StringFormatterMessageFactory.INSTANCE
    );
    protected static final JsonFactory JSON_FACTORY;

    static {
        // set up the JSON factory
        JSON_FACTORY = new JsonFactory();
        JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);

        LOGGER.debug("JSON factory successfully setup.");
    }
    @Override
    public void toJSON(final OutputStream out) throws IOException {
        if(out == null) {
            LOGGER.error("The output stream cannot be null.");
            throw new IOException("The output stream cannot be null.");
        }
        try {
            writeJSON(out);
        } catch (Exception e) {
            LOGGER.error("Unable to serialize the resource to JSON.", e);
            throw new IOException("Unable to serialize the resource to JSON.", e);
        }
    }
    protected void writeJSON(OutputStream out) throws IOException {
        out.write(
                new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer()
                .withRootName(this.getClass().getSimpleName())
                .writeValueAsString(this)
                .getBytes()
        );
    }
}