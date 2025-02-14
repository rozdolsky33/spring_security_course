package io.baselogic.springsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * Main Spring boot Application class
 *
 * @author mickknutson
 * @since chapter01.00 created
 * @since chapter03.01 Added passwordEncoding(ApplicationContext) to display encoded passwords.
 * @since chapter03.01 Now students can see the encoded user passwords for adding into the DB.
 */
@SpringBootApplication
@Slf4j
public class Application {


    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Utility to print out user password encoded.
     * These values can be used to change the encoded DB values based on the
     * {@link PasswordEncoder} implemented.
     * The default for this course is {@link BCrypt}
     *
     * @param ctx Application Context
     * @return CommandLineRunner
     */
    @Profile("trace")
    @Bean
    public CommandLineRunner passwordEncoding(ApplicationContext ctx) {
        return args -> {
            StringBuilder sb = new StringBuilder(1_000);

            sb.append("\n------------------------------------------------");
            sb.append("\nLets encrypt our standard passwords with our PasswordEncoder:");
            sb.append("\n------------------------------------------------");

            String[] passwords = {"user1", "admin1", "user2"};
            sb.append("\n\nEncoding passwords: ").append(Arrays.toString(passwords));
            sb.append("\n------------------------------------------------\n");

            for(String raw: passwords){
                String encoded = passwordEncoder.encode(raw);

                sb.append("\n[").append(raw).append("] ");
                sb.append("Matches [").append(encoded).append("] ");
                sb.append(": ").append(passwordEncoder.matches(raw, encoded));

                sb.append("\nValue for database: \n");
                sb.append("[").append(encoded).append("]");
                sb.append("\n------------------------------------------------\n");
            }

            sb.append("\n\n------------------------------------------------\n\n");

            log.info(sb.toString());
        };
    }


    /**
     * This simple utility will print out to the console, the beans that have bee loaded into the current context
     * @param ctx Application Context
     * @return CommandLineRunner
     */
    @Profile("trace")
    @Bean
    @Autowired
    public CommandLineRunner viewBeansInContext(final ApplicationContext ctx) {
        return args -> {

            StringBuilder sb = new StringBuilder(1_000);
            sb.append("\n------------------------------------------------");
            sb.append("Let's inspect the beans provided by Spring Boot:");
            sb.append("\n------------------------------------------------");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                sb.append(beanName);
            }

            sb.append("\n------------------------------------------------\n");

            log.debug(sb.toString());
        };
    }


} // The End...
