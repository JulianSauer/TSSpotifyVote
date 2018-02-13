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
                if (Main.DEBUG)
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
            if (Main.DEBUG)
                System.out.println("Saved config file under \"" + filePath + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }

    public void setProperty(String key, String value) {
        config.setProperty(key, value);
    }

    public void createConfig() {

        //initial values needed for first use
        for (CredentialsHolder c : CredentialsHolder.values()) {
            config.put(c.name(), c.toString());
        }
        if (Main.DEBUG)
            System.out.println("Created new config file.");

    }

    public static Config getInstance() {
        return conf;
    }


    public void deleteConfig() {
        if (new File(filePath).delete()) System.out.println("Deleted your config file under \"" + filePath + "\"");
    }
}
