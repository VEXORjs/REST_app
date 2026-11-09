package pas.v2026.pasrest.exceptions;

public class LoginAlreadyInUseException extends ConflictException{
    public LoginAlreadyInUseException(String login) {
        super(String.format("Użytkownik o loginie '%s' już istnieje.", login));
    }
}
