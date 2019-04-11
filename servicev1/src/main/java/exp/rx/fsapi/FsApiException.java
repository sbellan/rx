package exp.rx.fsapi;

public class FsApiException extends RuntimeException {
    private final int responseCode;
    private final String response;

    public FsApiException(String message, int responseCode, String response, Throwable cause) {
        super(String.format("%s-%d-%s", message, responseCode, response), cause);
        this.responseCode = responseCode;
        this.response = response;
    }
}
