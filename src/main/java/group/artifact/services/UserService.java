package group.artifact.services;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import group.artifact.dtos.impl.UserDTOImpl;
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
        try {
            boolean x = isCommonPassword(user.getPassword()); //check if password is on List
            //TODO: erneute aufforderung zur passworteingabe 
            System.out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            String pin = UUID.randomUUID().toString().substring(0, 5).toUpperCase(); // generate pin
            user.setPin(pin);

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

    public void unlock(String email) {
        User user = userRepository.findUserByEmail(email);
        user.setLocked(false);
        userRepository.save(user);
    }

    public boolean getLocked(String email){
        User user = userRepository.findUserByEmail(email);

        return user.getLocked();
    }

    public boolean authenticate(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if(user == null || user.getLocked()){
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
     * @returns: true -> password is on list
     *           false -> else
     */ 
public static boolean isCommonPassword(String passwd) throws IOException{
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try{
        br = new BufferedReader(new FileReader("src/main/resources/best1050.txt")); //might not survive packaging to jarfile
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
            if(passwd.equals(commonpw)){
                return true;
            }
        }
        return false;
    }

    public boolean pwLengthValid(String pw){
        return pw.length() >=8;
    }

    public boolean pwUpperCaseValid(String pw){
        return !pw.equals(pw.toLowerCase());
    }

    public boolean pwSpecialCharValid(String pw){
        String specialChars = "(.*[@,#,$,!,&,/,(,),=,?,%].*$)";
        return pw.matches(specialChars) ;
    }

    public boolean pwNumberValid(String pw){
        String numbers = "(.*[0,1,2,3,4,5,6,7,8,9].*$)";
        return pw.matches(numbers) ;
    }

    public UserDTO getUserDTO(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found"));
        UserDTO dto = new UserDTOImpl();
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public void updateUser(UserDTO userDTO, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found"));
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        userRepository.save(user);
    }

    public void generateVerificationCode(User user) {
        String pin = UUID.randomUUID().toString().substring(0, 5).toUpperCase(); // generate pin
        user.setPin(pin);
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public boolean removeUser(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    public boolean verfiyEmail(User user, String pin) {
        if (user.getPin().equals(pin)) {
            user.setPin("!true");
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
