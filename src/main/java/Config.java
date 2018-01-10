import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	private String filePath="Credentials.cfg";
	
	private Properties config;
	
	public Config() {
		config = new Properties();
		
		try {
			config.load(this.getClass().getResourceAsStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void SaveConfig() {
		try {
			config.store(new FileOutputStream(filePath),null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
