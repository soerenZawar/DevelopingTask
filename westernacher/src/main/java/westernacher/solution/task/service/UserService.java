package westernacher.solution.task.service;

import com.sun.jdi.request.DuplicateRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import westernacher.solution.task.dao.models.User;
import westernacher.solution.task.dao.repositories.UserRepository;
import westernacher.solution.task.resource.models.UserDTO;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Optional;

import static java.util.stream.Collectors.toCollection;

@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public UserService(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public Collection<UserDTO> getAllUser() {
        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .collect(toCollection(ArrayList::new));
    }

    public void removeUserById(int id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Value doesn't exist");
        }
        userRepository.deleteById(id);
    }

    public void removeAllUser() {
        userRepository.deleteAll();
    }

    public void updateUser(User user, int id) throws Exception {

        checkCorrectEmailAndBirthDate(user);

        userRepository.findById(id).map(u -> {

            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setBirthDate(user.getBirthDate());
            u.setEmail(user.getEmail());

            userRepository.save(u);

            return userRepository;
        }).orElseThrow(Exception::new);
    }

    public Optional<UserDTO> addUser(User user) {
        checkCorrectEmailAndBirthDate(user);

        Collection<UserDTO> res = getAllUser();

        for (UserDTO elem : res) {
            if (elem.getFirstName().equals(user.getFirstName())
                    && elem.getLastName().equals(user.getLastName())
                    && elem.getEmail().equals(user.getEmail())
                    && elem.getBirthDate().isEqual(user.getBirthDate())) {
                throw new DuplicateRequestException("Duplicated value");
            } else {
                return Optional.ofNullable(modelMapper.map(userRepository.save(user), UserDTO.class));
            }
        }
        return Optional.ofNullable(modelMapper.map(userRepository.save(user), UserDTO.class));
    }

    private void checkCorrectEmailAndBirthDate(User user) {
        if (user.getBirthDate() != null && LocalDate.now().minusYears(5).isBefore(user.getBirthDate())) {
            throw new InputMismatchException("To young for having an account");
        }

        if (user.getEmail() != null && !user.getEmail().contains("@")) {
            throw new InputMismatchException("No valid email address");
        }
    }


}
