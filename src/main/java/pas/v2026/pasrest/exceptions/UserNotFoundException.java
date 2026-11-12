package pas.v2026.pasrest.exceptions;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String param) {
        super(String.format("Użytkownik %s nie został znaleziony.", param));
    }
}
