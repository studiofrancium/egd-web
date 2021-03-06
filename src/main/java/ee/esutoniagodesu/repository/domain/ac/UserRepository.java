package ee.esutoniagodesu.repository.domain.ac;

import ee.esutoniagodesu.domain.ac.table.ExternalProvider;
import ee.esutoniagodesu.domain.ac.table.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime createdDate);

    @Query("select u from User u left join u.accountForm f where f.resetKey = ?1")
    Optional<User> findOneByResetKey(String resetKey);

    @Query("select u from User u where lower(u.email) = lower(?1)")
    Optional<User> findOneByEmail(String email);

    @Query("select u from User u where lower(u.email) = lower(?1) and u.uuid <> ?2")
    Optional<User> findOneByEmailNotThisUuid(String email, String uuid);

    @Query("select u from User u where u.uuid = ?1")
    Optional<User> findOneByUuid(String uuid);

    @Query("select u from User u left join u.accountForm f where lower(f.login) = lower(?1)")
    Optional<User> findOneByLogin(String login);

    @Override
    void delete(User t);

    @Query("select u from User u inner join u.accountExternals ea where ea.provider = ?1 and ea.identifier = ?2")
    Optional<User> findOneByExternalAccount(ExternalProvider provider, String identifier);
}
