package plugin;

public enum CredentialsHolder {
    IP(""),
    USERNAME(""),
    PASSWORD(""),
    SPOTIFY_TOKEN(""),
    CLIENT_ID(""),
    CLIENT_SECRET("");

    private final String value;

    CredentialsHolder(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
