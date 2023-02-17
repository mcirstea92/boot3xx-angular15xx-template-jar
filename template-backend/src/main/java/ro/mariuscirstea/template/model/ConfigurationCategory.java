package ro.mariuscirstea.template.model;

public enum ConfigurationCategory {

    BACKEND("BACKEND"), FRONTEND("FRONTEND");

    private final String _value;

    ConfigurationCategory(String value) {
        this._value = value;
    }

    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value;
    }
}
