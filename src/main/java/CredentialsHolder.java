public enum CredentialsHolder {
    IP(""),
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
