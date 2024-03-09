package origin;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BankClient {

    private static final String BASE_URL = "http://localhost:8080"; // Assuming server is running locally (replace if actual base URL of server is different)

    public static void main(String[] args) {
        getBalance();
        deposit(100);
        withdraw(50);
        getBalance();
    }

    public static void getBalance() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/balance"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Balance: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deposit(double amount) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/deposit?amount=" + amount))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.send(request, HttpResponse.BodyHandlers.discarding());
            System.out.println("Deposited: " + amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(double amount) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/withdraw?amount=" + amount))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.send(request, HttpResponse.BodyHandlers.discarding());
            System.out.println("Withdrawn: " + amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
