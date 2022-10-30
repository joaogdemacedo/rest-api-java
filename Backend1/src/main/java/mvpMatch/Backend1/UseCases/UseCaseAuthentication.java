package mvpMatch.Backend1.UseCases;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import mvpMatch.Backend1.DataProvider.User;
import mvpMatch.Backend1.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class UseCaseAuthentication {

    @Autowired
    UserRepository repository;

    public Argon2 getArgon2() {
        return argon2;
    }

    Argon2 argon2 = Argon2Factory.create();

    String loggedUsername;
    public Boolean validateBasicAuthentication(String basicAuthHeaderValue) {

        if (StringUtils.hasText(basicAuthHeaderValue) && basicAuthHeaderValue.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            // credentials = username:password
            final String[] values = decodeToken(basicAuthHeaderValue).split(":", 2);
            if (authentication(values[0],values[1])) {
                return true;
            }
        }
        return false;
    }

    public String getUsernameFromToken(String token){
        return decodeToken(token).split(":", 2)[0];
    }

    public boolean isAdmin(String username){
        Optional<User> usn = repository.findById(username);
        String role = usn.get().getRole();

       return repository.findById(username).get().getRole().equals("admin");
    }
    private String decodeToken(String token){
        String base64Credentials = token.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);

        return credentials;
    }

    private boolean authentication (String user, String password) {

        Optional<User> storedUser = repository.findById(user);

        if(storedUser!= null && argon2.verify(storedUser.get().getPassword(),password.toCharArray())){
            return true;
        }
        return false;
    }

    public String getLoggedUsername() {
        return loggedUsername;
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

}
