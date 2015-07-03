package ee.esutoniagodesu.config;

import ee.esutoniagodesu.security.social.SecurityUtilsUserIdSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Basic Spring Social configuration. Creates beans necessary to manage Connections to social services and
 * link accounts from those services to internal Users.
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SocialConfig.class);

    @Inject
    private ConnectionSignUp signup;

    @Inject
    private DataSource dataSource;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        // google configuration
        String googleClientId = environment.getProperty("spring.social.google.clientId");
        String googleClientSecret = environment.getProperty("spring.social.google.clientSecret");
        if (googleClientId != null && googleClientSecret != null) {
            log.debug("Configuring GoogleConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new GoogleConnectionFactory(
                    googleClientId,
                    googleClientSecret
                )
            );
        }

        // facebook configuration
        String facebookClientId = environment.getProperty("spring.social.facebook.clientId");
        String facebookClientSecret = environment.getProperty("spring.social.facebook.clientSecret");
        if (facebookClientId != null && facebookClientSecret != null) {
            log.debug("Configuring FacebookConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new FacebookConnectionFactory(
                    facebookClientId,
                    facebookClientSecret
                )
            );
        }

        /*
        // twitter configuration
        String twitterClientId = environment.getProperty("spring.social.twitter.consumerKey");
        String twitterClientSecret = environment.getProperty("spring.social.twitter.consumerSecret");
        if (twitterClientId != null && twitterClientSecret != null) {
            log.debug("Configuring FacebookConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new TwitterConnectionFactory(
                    twitterClientId,
                    twitterClientSecret
                )
            );
        }
        //*/
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new SecurityUtilsUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        InMemoryUsersConnectionRepository repo = new InMemoryUsersConnectionRepository(connectionFactoryLocator);

        //JdbcUsersConnectionRepository repo = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        //repo.setTablePrefix("ac.");

        // register our ConnectionSignUp so that UsersConnectionRepository can resolve external account ids
        // to internal Users
        repo.setConnectionSignUp(signup);

        return repo;
    }
}