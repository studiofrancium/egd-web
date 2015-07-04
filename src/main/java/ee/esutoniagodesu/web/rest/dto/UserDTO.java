package ee.esutoniagodesu.web.rest.dto;

import ee.esutoniagodesu.domain.ac.table.ExternalProvider;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @NotNull
    @Size(min = 8, max = 8)
    private String uuid;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(min = 2, max = 5)
    private String langKey;

    private List<String> roles;

    private final Map<ExternalProvider, String> externalAccounts = new HashMap<>();

    public UserDTO() {
    }

    public UserDTO(String uuid, String login, String password, String firstName, String lastName, String email, String langKey,
                   List<String> roles) {

        this.uuid = uuid;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
    }

    public UserDTO(String firstName, String lastName, String email, ExternalProvider externalProvider, String account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.externalAccounts.put(externalProvider, account);
    }

    public UserDTO(String login, String password, String firstName, String lastName, String email, String langKey) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Map<ExternalProvider, String> getExternalAccounts() {
        return Collections.unmodifiableMap(externalAccounts);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "uuid='" + uuid + '\'' +
            ", login='" + login + '\'' +
            ", password='" + password + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", langKey='" + langKey + '\'' +
            ", roles=" + roles +
            ", externalAccounts=" + externalAccounts +
            '}';
    }
}
