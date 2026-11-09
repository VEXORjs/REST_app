package pas.v2026.pasrest.exceptions;

public class AllocationAlreadyFinishedException extends ConflictException {
    public AllocationAlreadyFinishedException() {
        super("Ta alokacja jest już zakończona (miniona) i nie można na niej wykonać tej operacji.");
    }
}
