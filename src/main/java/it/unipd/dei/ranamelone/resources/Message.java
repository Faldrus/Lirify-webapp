package it.unipd.dei.ranamelone.resources;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a message or an error message.
 */
public class Message extends AbstractResource {
    private final String message;
    private final String errorCode;
    private final String errorDetails;
    private final boolean isError;

    public Message(String message, String errorCode, String errorDetails) {
        this.message = message;
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
        this.isError = true;
    }

    public Message(String message) {
        this.message = message;
        this.errorCode = null;
        this.errorDetails = null;
        this.isError = false;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public boolean isError() {
        return isError;
    }

    @Override
    protected void writeJSON(OutputStream out) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this);
        out.write(json.getBytes());
    }
}