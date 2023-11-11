package group.artifact.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import group.artifact.entities.User;
import group.artifact.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserService {
    private final SecureRandom RANDOM = new SecureRandom();
    private UserRepository userRepository;

    public void createUser(User newUser) {
        if (!userRepository.isEmailUnique(newUser.getEmail())) { // check if email already exists
            return; // TODO sinnvolle ausgabe + handling
        }
        try {
            newUser.setSalt(generateSalt(16)); // generate salt
            newUser.setPassword(generateHash(newUser.getPassword(), newUser.getSalt())); // hash pw
            userRepository.save(newUser); // save to DB
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: unable to insert" + newUser.toString());
        }
    }

    public boolean authentificate(String email, String password) {  //sollte auth nicht über email laufen, vor und zunamen können sich doppel und werden nicht auf uniqueness überprüft
        User user=userRepository.findUserByEmail(email);

        if(user== null){
            Notification.show("Falsche Email oder Passwort! ").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }else{
            String salt = user.getSalt();
            String hash = generateHash(password, salt);

            String tmp= user.getPassword();
            if(!hash.equals(tmp)){
                Notification.show("Falsche Email oder Passwort!").addThemeVariants(NotificationVariant.LUMO_ERROR);

            }else{
                Notification.show("Login erfolgreich!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                return true; //session startet aktuell in UserController.login()
            }
        }
        return false;
    }

    /**
     * generate salt of specified length
     * 
     * @param len: length for the result
     * @return: usable salt as string
     */
    private String generateSalt(int len) {
        byte bytes[] = new byte[len];
        RANDOM.nextBytes(bytes);
        Encoder encoder = Base64.getEncoder().withoutPadding();
        String salt = encoder.encodeToString(bytes);
        return salt;
    }

    /**
     * hash a given password with the given salt
     * 
     * @param password: users pw
     * @param salt: generated salt
     * @return: storable hash
     */
    private String generateHash(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            Encoder encoder = Base64.getEncoder().withoutPadding();
            return encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException NSAE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (InvalidKeySpecException IKSE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /*
    *@params: user email to get userid
    *Generate a cookie for a user with given userid + SID (easy uniqueness)
    *@return: jakarta.http.cookie object
    * 
    */
    public Cookie setSessionCookie(String email){   
        User user=userRepository.findUserByEmail(email);
        int uid = user.getUser_pk();
        String sid = generateSalt(20); //saltgeneration gibt random string len bytes, 16 bytes empfehlung OWASP
        Cookie sessionCookie = new Cookie("sid", uid + sid);
        sessionCookie.setMaxAge(1200);  //expire in 20 min
        sessionCookie.setPath("/");
        sessionCookie.setAttribute("SameSite","Lax");
        sessionCookie.setHttpOnly(true);
        return sessionCookie;
    }

    /*
     * revokes a cookie for logout
     * @param: cookie: current sessioncookie
     * returns: jakarta.http.cookie object
     */
    public Cookie revokeCookie(Cookie cookie){
        cookie.setMaxAge(0);
        return cookie;
    }
}
