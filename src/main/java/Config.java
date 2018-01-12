import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private String filePath = "Credentials.cfg";

    private Properties config;

    Config() {
        config = new Properties();

        try {
            config.load(this.getClass().getResourceAsStream(filePath));
        } catch (Exception e) {
            System.err.println("Could not find config file");
        }

        //TODO implement first use scenario, creating config file and informing user to edit it
        //initial values needed for first use
        config.setProperty("IP", "");
        config.setProperty("USERNAME", "");
        config.setProperty("PASSWORD", "");

        SaveConfig();
    }


    public void SaveConfig() {
        try {
            config.store(new FileOutputStream(filePath), null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }

}
