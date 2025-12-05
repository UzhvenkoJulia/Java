import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.temporal.ChronoUnit;


public class B11_04 {

    private static final String TIME_URL = "https://time.is/Kyiv";
    private static final String TIMEZONE = "Europe/Kyiv";

    public static void main(String[] args) {
        try {
            String exactTimeStr = getExactTimeFromWebsite(TIME_URL);

            if (exactTimeStr != null) {
                System.out.println("✅ Точний час із сайту " + TIME_URL + ": **" + exactTimeStr + "**");

                LocalTime localTime = getLocalKyivTime();
                String localTimeStr = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                System.out.println("⏱️ Час на локальному комп'ютері (у часовому поясі Києва): **" + localTimeStr + "**");

                compareTimes(exactTimeStr, localTime);
                
            } else {
                System.out.println("❌ Не вдалося отримати точний час із сайту");
            }

        } catch (Exception e) {
            System.err.println("❌ Виникла помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * HTTP-запит до сайту та витяг час за допомогою регулярного виразу.
     * @param urlString Адреса сайту.
     * @return Час у форматі "HH:MM:SS" або null у разі помилки
     */
     
    private static String getExactTimeFromWebsite(String urlString) throws Exception {
        URL url = new URL(urlString);
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestProperty("User-Agent", "Java/WebClient (Compatibility)"); 
        
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.err.println("❌ Помилка HTTP-запиту. Код відповіді: " + responseCode);
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        // <time id="clock">, наприклад:
        // <time id="clock">12:00:00</time>
        
        // Патерн: <time id="clock"> (захоплююча група 1: HH:MM:SS) </time>
        // ([0-9]{2}:[0-9]{2}:[0-9]{2}) - захоплює час у форматі ЧЧ:ХХ:СС
        
        Pattern pattern = Pattern.compile("<time id=\"clock\">([0-9]{2}:[0-9]{2}:[0-9]{2})</time>");
        Matcher matcher = pattern.matcher(content.toString());

        if (matcher.find()) {
            return matcher.group(1); 
        } else {
            return null; 
        }
    }
    
    private static LocalTime getLocalKyivTime() {
        ZoneId kyivZone = ZoneId.of(TIMEZONE);
        LocalDateTime nowKyiv = LocalDateTime.now(kyivZone);
        
        return nowKyiv.toLocalTime();
    }
    
    private static void compareTimes(String exactTimeStr, LocalTime localTime) {
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime exactTime = LocalTime.parse(exactTimeStr, timeFormatter);
        
        long diffSeconds = ChronoUnit.SECONDS.between(exactTime, localTime.truncatedTo(ChronoUnit.SECONDS));

        System.out.println("\n--- Результат порівняння ---");
        
        if (diffSeconds == 0) {
            System.out.println("🟢 **ЧАС ЗБІГАЄТЬСЯ** (різниця менше 1 секунди)");
        } else {
            System.out.println("🔴 **ЧАС НЕ ЗБІГАЄТЬСЯ**.");
            // модуль
            System.out.println("Різниця становить: **" + Math.abs(diffSeconds) + "** секунд(и)");
        }
    }
}