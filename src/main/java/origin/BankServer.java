package origin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class BankServer {

    private double balance = 0; // Assuming single account for simplicity

    @GetMapping("/balance")     // GET request to retrieve the account balance
    public double getBalance() {
        return balance;
    }

    @PostMapping("/deposit")    // POST request to add money to the account
    public void deposit(@RequestParam double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
    }

    @PostMapping("/withdraw")   // POST request to remove money from the account
    public void withdraw(@RequestParam double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BankServer.class, args);
    }
}
