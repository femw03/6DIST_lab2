package origin;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankClient {

    private static final String BASE_URL = "http://localhost:8080"; // Assuming server is running locally

    /*public static void main(String[] args) {
        BankClient client = new BankClient();
        client.testEndpoints();
    }*/

    private static final int NUM_CLIENTS = 2; // Number of client instances

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_CLIENTS);

        for (int i = 0; i < NUM_CLIENTS; i++) {
            executorService.submit(() -> {
                BankClient client = new BankClient();
                client.testEndpoints();
            });
        }

        executorService.shutdown();
    }

    public void testEndpoints() {
        try {
            System.out.println("Balance account '123': " + getBalance("123"));
            deposit("123", 135);
            withdraw("123", 15);
            System.out.println("Balance account '123': " + getBalance("123"));
            System.out.println();

            System.out.println("Balance account '456': " + getBalance("456"));
            deposit("456", 20);
            withdraw("456", 5);
            System.out.println("Balance account '456': " + getBalance("456"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBalance(String accountNumber) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/balance/" + accountNumber))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Double.parseDouble(response.body());
    }

    public void deposit(String accountNumber, double amount) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/deposit/" + accountNumber + "?amount=" + amount))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(String accountNumber, double amount) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/withdraw/" + accountNumber + "?amount=" + amount))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println("Withdrawn: " + amount);
    }
}
