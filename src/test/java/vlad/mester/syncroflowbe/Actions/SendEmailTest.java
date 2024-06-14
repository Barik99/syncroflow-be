package vlad.mester.syncroflowbe.Actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SendEmailTest {

    private SendEmail sendEmail;

    @BeforeEach
    public void setUp() {
        this.sendEmail = new SendEmail("name", "vladi.mester@gmail.com", "Test vlad", "Ozzie is the best!");
    }

    @Test
    public void execute_sendsEmailSuccessfully() {
        assertTrue(sendEmail.execute());
    }
}