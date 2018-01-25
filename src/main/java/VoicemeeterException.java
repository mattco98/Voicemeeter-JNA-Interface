/**
 * Indicates an error has occurred when trying to
 * interface with Voicemeeter.
 */
public class VoicemeeterException extends RuntimeException {
    public VoicemeeterException() {
        super();
    }

    public VoicemeeterException(String message) {
        super(message);
    }
}
