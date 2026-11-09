package pas.v2026.pasrest.exceptions;

public class ResourceUnavailableException extends ConflictException {

    public ResourceUnavailableException() {
        super("Wybrany zasób ma status niedostępnego.");
    }

    public ResourceUnavailableException(String resourceName) {
        super(String.format("Zasób '%s' ma status niedostępnego.", resourceName));
    }
}
