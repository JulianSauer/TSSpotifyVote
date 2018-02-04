package plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Config conf = new Config();

    private String filePath = System.getProperty("user.dir") + File.separator + "Credentials.cfg";

    private Properties config;

    Config() {
        config = new Properties();

        if ((new File(filePath)).exists()) {
            try {
                System.out.println("Trying to read file from \"" + filePath + "\"");
                config.load(new FileInputStream(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            createConfig();
            saveConfig();
        }

    }


    public void saveConfig() {
        try {
            config.store(new FileOutputStream(filePath), null);
            System.out.println("Saved config file under \"" + filePath + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }


    public void createConfig() {

        //initial values needed for first use
        for (CredentialsHolder c : CredentialsHolder.values()) {
            config.put(c.name(), c.toString());
        }
        config.put("EULA", "false");
        System.out.println("Created new config file. Please enter your information here properly and change the field eula to \"true\"");

    }

    public static Config getInstance() {
        return conf;
    }


    public void deleteConfig() {
        if (new File(filePath).delete()) System.out.println("Deleted your config file under \"" + filePath + "\"");
    }
}
