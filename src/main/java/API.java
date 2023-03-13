import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class API {
    private Map<String, Double> rates = new HashMap<String, Double>();

    public static Map<String, Double> accessAPI() {

        StringBuilder information = new StringBuilder();

        try {
            URL url = new URL("https://api.currencybeacon.com/v1/latest?api_key=bb017cfe3c7311e2f3c774f8af75c55a");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (!(responseCode == 200)) {
                throw new RuntimeException("Connection unsuccessful");
            } else {
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    information.append(scanner.nextLine());
                }
                scanner.close();
            }
        } catch (IOException e) {
            System.out.println("IO Exception: Connection to API unsuccessful.");
        }

        String infoFullString = information.toString();

        String infoSubString = infoFullString.substring((infoFullString.indexOf("ADA") - 1), infoFullString.indexOf("}}}"));
        String[] infoArray = infoSubString.split(",");

        Map<String, Double> rates = new HashMap<>();

        for (String rate : infoArray) {
            String[] subArray = rate.split(":");
            String key = subArray[0].replace("\"", "");
            rates.put(key, Double.valueOf(subArray[1]));
        }

        return rates;
    }
}