package group.artifact.services;

import org.springframework.stereotype.Service;

import group.artifact.entities.Session;
import group.artifact.repositories.SessionRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SessionService {

    private SessionRepository sessionRepository;

    public boolean validateSession(String sid) {
        Session session = sessionRepository.findBySid(sid);
        if (session != null) { // sid exists
            return true;
        }
        return false;
    }
}
