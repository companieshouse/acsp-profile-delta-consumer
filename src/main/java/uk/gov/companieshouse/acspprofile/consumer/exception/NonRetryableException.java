package uk.gov.companieshouse.acspprofile.consumer.exception;

public class NonRetryableException extends RuntimeException {

    public NonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonRetryableException(String message) {
        super(message);
    }
}
