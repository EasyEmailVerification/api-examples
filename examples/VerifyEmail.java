import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class VerifyEmail {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("EEV_API_KEY");
        String email = args.length > 0 ? args[0] : "user@example.com";

        if (apiKey == null || apiKey.isBlank()) {
            System.err.println(
                "EEV_API_KEY is not set. Create/manage a key at " +
                "https://dashboard.easyemailverification.com/apisettings"
            );
            System.exit(1);
        }

        String query =
            "email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
            "&apikey=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        URI uri = URI.create(
            "https://api.easyemailverification.com/v1/verify?" + query
        );

        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        HttpRequest request = HttpRequest.newBuilder(uri)
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                "EEV request failed with HTTP " + response.statusCode()
            );
        }

        System.out.println(response.body());
    }
}
