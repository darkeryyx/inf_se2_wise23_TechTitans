package group.artifact.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import group.artifact.entities.User;
import group.artifact.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final SecureRandom RANDOM = new SecureRandom();
    private UserRepository userRepository;

    public void createUser(User newUser) {
        if (!userRepository.isEmailUnique(newUser.getEmail())) { // check if email already exists
            return; // TODO
        }
        try {
            newUser.setSalt(generateSalt(16)); // generate salt
            newUser.setPassword(generateHash(newUser.getPassword(), newUser.getSalt())); // hash pw
            userRepository.save(newUser); // save to DB
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: unable to insert" + newUser.toString());
        }
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

}
