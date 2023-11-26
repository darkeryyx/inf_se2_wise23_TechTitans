package group.artifact.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import group.artifact.dtos.UserDTO;
import group.artifact.entities.Session;
import group.artifact.entities.User;
import group.artifact.repositories.SessionRepository;
import group.artifact.repositories.UserRepository;
import javax.servlet.http.Cookie;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final SecureRandom RANDOM = new SecureRandom();
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public String createUser(UserDTO newUser) {
        User user = newUser.getUser();
        if (!userRepository.isEmailUnique(user.getEmail())) { // check if email already exists
            return "email_error";
        }
        try {
            user.setSalt(generateSalt(16)); // generate salt
            user.setPassword(generateHash(user.getPassword(), user.getSalt())); // hash pw

            String hashedAnswer1 = generateHash(user.getAnswer1(), user.getSalt());
            user.setAnswer1(hashedAnswer1);
            String hashedAnswer2 = generateHash(user.getAnswer2(), user.getSalt());
            user.setAnswer2(hashedAnswer2);

            userRepository.save(user); // save to DB
            return "true";

        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: unable to insert" + user.toString());
            return "false";
        }
    }

    public void lock(String email){
        User user = userRepository.findUserByEmail(email);
        user.setLocked(true);
        userRepository.save(user);
    }

    public boolean getLocked(String email){
        User u= userRepository.findUserByEmail(email);

        return u.isLocked();
    }

    public boolean authenticate(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if(user.isLocked()){
            return false;
        }

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

    public List<String> getQuestions(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return null;
        } else {
            List<String> questions = new ArrayList<>();
            questions.add(user.getQuestion1());
            questions.add(user.getQuestion2());
            return questions;
        }
    }

    public boolean checkSQA(String frage, String antwort, String email) {
        User user = userRepository.findUserByEmail(email);
        String solution = "";
        String salt = user.getSalt();
        String hashedAnswer = generateHash(antwort, salt);

        if (frage.equals(user.getQuestion1())) {
            solution = user.getAnswer1();
        } else if (frage.equals(user.getQuestion2())) {
            solution = user.getAnswer2();
        }
        if (hashedAnswer.equals(solution)) {
            return true;
        }
        return false;
    }

    public void pwNew(String email, String pw) {
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
        String sid = rnd + uid;

        Cookie sessionCookie = new Cookie("sid", sid);
        sessionCookie.setMaxAge(1200); // expire in 20 min
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
       //sessionCookie.sameSite("Lax"); 
       



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
     * @returns: javax.http.cookie object
     */
    public Cookie revokeCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        return cookie;
    }

    public User getCurrentUser(Cookie[] cookies) { // get the currently logged in User
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

        /*
     * reads commonpasswordlist and compares it with userpassword
     * 
     * @param: String: registration form user password
     * 
     * @returns: false -> password is on list
     *           true -> else
     */ 
public static boolean isCommonPassword(String password) throws IOException{
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try{
        br = new BufferedReader(new FileReader("./best1050.txt"));
        String line;
        while((line = br.readLine())!= null){
            list.add(line);
        }
        } catch(IOException e){
            e.printStackTrace();
        }finally{
            if (br != null){
            br.close();
            }
        }
        for(String commonpw : list){
            if password.equals(commonpw){
                return true;
            }
        }
        return false;
    }    
}
