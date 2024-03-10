package origin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@RestController
public class BankServer {

    private Map<String, Double> accounts = new ConcurrentHashMap<>(); // Store account balances by account number

    @GetMapping("/balance/{accountNumber}")         // GET request to retrieve the account balance
    public double getBalance(@PathVariable String accountNumber) {
        return accounts.getOrDefault(accountNumber, 0.0);
    }

    @PostMapping("/deposit/{accountNumber}")        // POST request to add money to the account
    public synchronized void deposit(@PathVariable String accountNumber, @RequestParam double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        accounts.put(accountNumber, accounts.getOrDefault(accountNumber, 0.0) + amount);
    }

    @PostMapping("/withdraw/{accountNumber}")       // POST request to remove money from the account
    public synchronized void withdraw(@PathVariable String accountNumber, @RequestParam double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (!accounts.containsKey(accountNumber) || accounts.get(accountNumber) < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        accounts.put(accountNumber, accounts.get(accountNumber) - amount);
    }

    public static void main(String[] args) {
        SpringApplication.run(BankServer.class, args);
    }
}
