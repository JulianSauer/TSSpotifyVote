package plugin;

public enum CredentialsHolder {
    IP(""),
    SPOTIFY_TOKEN(""),
    CLIENT_ID(""),
    CLIENT_SECRET(""),
    USERNAME("VoteBot"),
    BOTNAME("Music Bot"),
    PASSWORD("");

    private final String value;

    CredentialsHolder(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
