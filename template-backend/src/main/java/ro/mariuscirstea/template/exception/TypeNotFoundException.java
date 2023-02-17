package ro.mariuscirstea.template.exception;

public class TypeNotFoundException extends RuntimeException {

    public TypeNotFoundException(String type, long id) {
        super("Entity type " + type + " was not found for id " + id);
    }

    public TypeNotFoundException(String type, String message) {
        super("Entity type " + type + " was not found. " + message);
    }

}
