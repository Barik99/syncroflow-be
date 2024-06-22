package vlad.mester.syncroflowbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import vlad.mester.syncroflowbe.services.LoadRuleControllerService;

import java.io.File;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SyncroflowBEApplication {

    public static void main(String[] args) {
        File file = new File("FileDirectory");
        if (!file.exists()) {
            file.mkdir();
        }
        new LoadRuleControllerService();
        SpringApplication.run(SyncroflowBEApplication.class, args);
    }
}