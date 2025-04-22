package parking_lot.util;

import parking_lot.ParkingLot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoaderUtil {


    public Properties loadAndFetchProperties(String configFilePath) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = ParkingLot.class.getClassLoader().getResourceAsStream(configFilePath);
            properties.load(inputStream);
        } catch (Exception e) {
            System.out.println("Exception in loading properties = "+e.getMessage());
        } finally {
            if(inputStream!=null) inputStream.close();
        }
        return properties;
    }
}
