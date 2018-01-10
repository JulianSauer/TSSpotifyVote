public enum CredentialsHolder {
    IP("192.168.0.164"),
    USERNAME("VoteBot"),
    PASSWORD("elFUF6Vq");

    private final String value;

    CredentialsHolder(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
