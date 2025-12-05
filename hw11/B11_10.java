import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Приклад запуску: java B11_10 Kyiv
 */
 
public class B11_10 {

    private static final String BASE_URL = "https://www.timeanddate.com/weather/ukraine/";
    // <td class="rbi">...</td>           <-- Стовпець часу
    // <td class="rbi temp">...</td>      <-- Стовпець температури
    // ...
    // <td class="rbi">...</td>           <-- Стовпець вологості (Humidity)
    
    // Патерн шукає наступне:
    // 1. Час: <td class="rbi">(.*?)</td>      (Група 1: Час)
    // 2. Температура: <td class="rbi temp">([+-]?\\d+)&deg;C</td> (Група 2: Температура, наприклад, "+15")
    // 3. Вологість: <td class="rbi"\\s*>(\\d+)%</td> (Група 3: Вологість, наприклад, "75")
    
    private static final String WEATHER_REGEX = 
        "<td class=\"rbi\">(.*?)</td>.*?<td class=\"rbi temp\">([+-]?\\d+)&deg;C</td>.*?<td class=\"rbi\"\\s*>(\\d+)%</td>";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("❌ Помилка: Будь ласка, введіть англійську назву міста як параметр");
            System.out.println("Приклад: java B11_10 kyiv");
            return;
        }

        String city = args[0].toLowerCase(); 
        String fullUrl = BASE_URL + city;

        System.out.println("🌍 Запит прогнозу погоди для міста: **" + city.toUpperCase() + "**");
        System.out.println("URL: " + fullUrl);
        System.out.println("---...---");

        try {
            List<WeatherEntry> forecast = getForecastFromWebsite(fullUrl);

            if (forecast.isEmpty()) {
                System.out.println("❌ Не вдалося отримати прогноз погоди. Перевірте назву міста або структуру сайту");
            } else {
                System.out.println("Прогноз на 48 годин (Температура та Вологість):");
                System.out.printf("%-15s %-15s %-15s%n", "Час", "Температура", "Вологість");
                System.out.println("--- ---");
                
                int limit = Math.min(forecast.size(), 14);
                for (int i = 0; i < limit; i++) {
                    WeatherEntry entry = forecast.get(i);
                    System.out.printf("%-15s %-15s %-15s%n", 
                                      entry.time, 
                                      entry.temperature + "°C", 
                                      entry.humidity + "%");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Виникла помилка: " + e.getMessage());
        }
    }

    private static List<WeatherEntry> getForecastFromWebsite(String urlString) throws Exception {
        List<WeatherEntry> forecast = new ArrayList<>();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // web scraping
        connection.setRequestProperty("User-Agent", "Java/WebClient (Weather-Scraper)");
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP-помилка. Код відповіді: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        Pattern pattern = Pattern.compile(WEATHER_REGEX);
        Matcher matcher = pattern.matcher(content.toString());

        while (matcher.find()) {
            String time = matcher.group(1).trim(); 
            String temp = matcher.group(2).trim(); 
            String humidity = matcher.group(3).trim(); 

            if (!time.isEmpty() && time.contains(":")) {
                forecast.add(new WeatherEntry(time, temp, humidity));
            }
        }
        return forecast;
    }

    private static class WeatherEntry {
        String time;
        String temperature;
        String humidity;

        public WeatherEntry(String time, String temperature, String humidity) {
            this.time = time;
            this.temperature = temperature;
            this.humidity = humidity;
        }
    }
}

// javac B11_10.java
// java B11_10 kyiv (java B11_10 lviv)