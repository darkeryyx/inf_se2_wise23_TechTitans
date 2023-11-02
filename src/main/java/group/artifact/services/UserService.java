package group.artifact.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import group.artifact.entities.User;
import group.artifact.repositories.SessionRepository;
import group.artifact.repositories.UserRepository;

@Service
public class UserService {
    private final SecureRandom RANDOM= new SecureRandom();
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    
    @Autowired  
    public void UserService(UserRepository userRepository, SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public String createUser(User newUser){
       if(!userRepository.isUnique(newUser.getUsername())){ //check if username already exists
            return null;
       }
       try{
        newUser.setSalt(generateSalt(16));          //generate salt
        newUser.setPassword(generateHash(newUser.getPassword(), newUser.getSalt()));   //hash pw 
        userRepository.save(newUser);                       //save to DB
        return "1";     //test
       }catch(DataIntegrityViolationException e){
            System.out.println("Error: unable to insert" + newUser.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
       }

    }

    private String generateSalt(int len){
        byte bytes[] =new byte[len];
        RANDOM.nextBytes(bytes);
        Encoder encoder = Base64.getEncoder().withoutPadding();
        String salt=encoder.encodeToString(bytes);
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
    
    
}
