package rewards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

// TODO-00 : In this lab, you are going to exercise the following:
// - Understanding how auto-configuration is triggered in Spring Boot application
// - Using auto-configuring DataSource in test and application code
// - Understanding how @SpringBootTest is used to create application context in test
// - Implementing CommandLineRunner using auto-configured JdbcTemplate
// - Disabling a particular auto-configuration
// - Exercising the usage of @ConfigurationProperties

// --------------------------------------------

// TODO-11 (Optional) : Disable 'DataSource' auto-configuration
// - Note that you are using your own 'DataSource' bean now
//   instead of auto-configured one
// - Use 'exclude' attribute of '@SpringBootApplication'
//   excluding 'DataSourceAutoConfiguration' class
// - Run this application and observe a failure
// - Import 'RewardsConfig' class
// - Run this application again and observe a successful execution

// TODO-12 (Optional) : Look in application.properties for the next step.

// TODO-13 (Optional) : Follow the instruction in the lab document.
//           The section titled "Build and Run using Command Line tools".

@SpringBootApplication
//@ConfigurationPropertiesScan
@EnableConfigurationProperties(RewardsRecipientProperties.class)
public class RewardsApplication {
    static final String SQL = "SELECT count(*) FROM T_ACCOUNT";

    final Logger logger
            = LoggerFactory.getLogger(RewardsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate) {
        Long numberOfAccounts = jdbcTemplate.queryForObject(SQL, Long.class);
        return args -> logger.info("Hello, there are {} accounts.", numberOfAccounts);
    }

    @Bean
    CommandLineRunner commandLineRunner2(RewardsRecipientProperties rewardsRecipientProperties) {
        return args -> logger.info("Recipient: {}", rewardsRecipientProperties.getName());
    }

}
