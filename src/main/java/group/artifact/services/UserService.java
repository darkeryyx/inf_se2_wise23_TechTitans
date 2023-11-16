package group.artifact.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.Base64.Encoder;
import java.util.stream.Collectors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import group.artifact.entities.Session;
import group.artifact.entities.User;
import group.artifact.repositories.SessionRepository;
import group.artifact.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final SecureRandom RANDOM = new SecureRandom();
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public String createUser(User newUser) {
        if (!userRepository.isEmailUnique(newUser.getEmail())) { // check if email already exists
            return "email_error";
        }
        try {
            newUser.setSalt(generateSalt(16)); // generate salt
            newUser.setPassword(generateHash(newUser.getPassword(), newUser.getSalt())); // hash pw
            userRepository.save(newUser); // save to DB
            return "true";
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: unable to insert" + newUser.toString());
            return "false";
        }
    }

    public boolean authenticate(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return false;
        }
        String salt = user.getSalt();
        String tmp = user.getPassword();
        String hash = generateHash(password, salt);

        if (!hash.equals(tmp)) {
            return false;
        }
        return true; // session startet aktuell in UserController.login()
    }

    public List<String> getQuestions(String email){
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return Collections.emptyList();
        }else{
           return getSecurityQuestions(user);

        }
    }
    private List<String> getSecurityQuestions(User user){
        Map<String, String> map= user.getSQA();

        return map.keySet().stream().collect(Collectors.toList());
    }

    public boolean checkSQA(String frage, String antwort, String email){
        User user= userRepository.findUserByEmail(email);

        if(antwort.equals(user.getSQA().get(frage))){
            return true;
        }
        return false;
    }

    public void pwNew(String email,String pw){
        User user = userRepository.findUserByEmail(email);
        user.setPassword(generateHash(pw, user.getSalt()));
        userRepository.save(user);
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
     * @param salt:     generated salt
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
     * generate a cookie for a user with given userid + SID (easy uniqueness)
     * 
     * @params: user email to get userid
     * 
     * @return: jakarta.http.cookie object
     * 
     */
    public Cookie setSessionCookie(String email) {
        Session newSession = new Session();
        User user = userRepository.findUserByEmail(email);
        String uid = Integer.toString(user.getUser_pk());
        String rnd = generateSalt(20); // saltgeneration gibt random string len bytes, 16 bytes empfehlung OWASP
        String sid = uid + rnd;

        Cookie sessionCookie = new Cookie("sid", sid);
        sessionCookie.setMaxAge(1200); // expire in 20 min
        sessionCookie.setPath("/");
        sessionCookie.setAttribute("SameSite", "Lax");
        sessionCookie.setHttpOnly(true);

        newSession.setSid(sid);
        newSession.setUser(user);
        newSession.setLogin(null);
        sessionRepository.save(newSession);
        return sessionCookie;
    }

    /*
     * revokes a cookie for logout
     * 
     * @param: cookie: current sessioncookie
     * 
     * @returns: jakarta.http.cookie object
     */
    public Cookie revokeCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        return cookie;
    }

    public User getCurrentUser(Cookie[] cookies) { //get the currently logged in User
        String name = "sid";
        User user = null;
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                Session session = sessionRepository.findBySid(cookie.getValue());
                user = session.getUser();
            }
        }
        return user;
    }
}
