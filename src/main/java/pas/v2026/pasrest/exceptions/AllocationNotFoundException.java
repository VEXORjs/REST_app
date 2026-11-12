package pas.v2026.pasrest.exceptions;

public class AllocationNotFoundException extends NotFoundException {
    public AllocationNotFoundException(String id) {
      super(String.format("Alokacja o identyfikatorze %s nie została znaleziona", id));
    }

}
