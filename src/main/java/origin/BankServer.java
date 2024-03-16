package origin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@RestController
public class BankServer {

    private Map<String, Double> accounts = new ConcurrentHashMap<>(); // Store account balances by account number

    @GetMapping("/balance/{accountNumber}")         // GET request to retrieve the account balance
    public ResponseEntity<Double> getBalance(@PathVariable String accountNumber) {
        Double balance = accounts.getOrDefault(accountNumber, 0.0);
        if (balance != null) {
            return ResponseEntity.ok(balance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deposit/{accountNumber}")        // POST request to add money to the account
    public synchronized ResponseEntity<Object> deposit(@PathVariable String accountNumber, @RequestParam double amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("ERROR: Deposit amount must be positive");
        }
        accounts.put(accountNumber, accounts.getOrDefault(accountNumber, 0.0) + amount);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/withdraw/{accountNumber}")       // POST request to remove money from the account
    public synchronized ResponseEntity<String> withdraw(@PathVariable String accountNumber, @RequestParam double amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("ERROR: Withdrawal amount must be positive");
        }
        if (!accounts.containsKey(accountNumber) || accounts.get(accountNumber) < amount) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: Insufficient balance");
        }
        accounts.put(accountNumber, accounts.get(accountNumber) - amount);
        return ResponseEntity.noContent().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(BankServer.class, args);
    }
}
