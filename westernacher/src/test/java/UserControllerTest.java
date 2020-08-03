import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringRunner;
import westernacher.solution.task.dao.models.User;
import westernacher.solution.task.dao.repositories.UserRepository;
import westernacher.solution.task.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Test()
    public void getAllUser_userSize_executed() {
        when(userRepository.findAll())
                .thenReturn(Stream.of(createUser(), createUser()).collect(Collectors.toList()));

        assertEquals(2, userService.getAllUser().size());
    }

    @Test
    public void getAllUser_wrongUserSize_notExecuted() {
        when(userRepository.findAll())
                .thenReturn(Stream.of(createUser(), createUser()).collect(Collectors.toList()));

        assertNotEquals(3, userService.getAllUser().size());
    }

    @Test
    public void updateUser_findByIdAndSave_executed() throws Exception {
        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(createUser()));
        userService.updateUser(createUser(), 1);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUser_notFindById_notExecuted() throws Exception {
        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(createUser()));
        userService.updateUser(createUser(), 0);

        verify(userRepository, never()).findById(1);
    }

    @Test
    public void updateUser_inputMismatchExceptionWrongEmail_notExecuted() {
        User user = createUser();
        user.setEmail("testWithoutAtSymbol");

        Throwable exception = assertThrows(InputMismatchException.class,
                ()-> userService.updateUser(user,0));
        assertEquals("No valid email address", exception.getMessage());
    }

    @Test
    public void updateUser_findByIdNotSave_notExecuted() {
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateUser_inputMismatchExceptionWrongBirthDate_notExecuted() {
        User user = createUser();
        user.setBirthDate(LocalDate.now());

        Throwable exception = assertThrows(InputMismatchException.class,
                ()-> userService.updateUser(user,0));
        assertEquals("To young for having an account", exception.getMessage());
    }

    @Test
    public void addUser_checkSave_executed() {
        User newUser = new User();
        setUserData(newUser);

        when(userRepository.save(newUser)).thenReturn(newUser);
        userService.addUser(newUser);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test()
    public void addUser_InputMismatchExceptionWrongEmail_notExecuted() {
        User user = createUser();
        user.setEmail("testWithoutAtSymbol");

        Throwable exception = assertThrows(InputMismatchException.class,
                ()-> userService.addUser(user));
        assertEquals("No valid email address", exception.getMessage());
    }

    @Test()
    public void addUser_InputMismatchExceptionWrongBirthDate_notExecuted() {
        User user = createUser();
        user.setBirthDate(LocalDate.now());

        Throwable exception = assertThrows(InputMismatchException.class,
                ()-> userService.addUser(user));
        assertEquals("To young for having an account", exception.getMessage());
    }

    @Test()
    public void removeUserById_executed() {
        User user = createUser();
        user.setId(2);
        when(userRepository.existsById(2)).thenReturn(true);
        userService.removeUserById(2);
        verify(userRepository, times(1)).deleteById(2);
    }

    @Test()
    public void removeUserById_entityNotFoundException_notExecuted() {
        Throwable exception = assertThrows(EntityNotFoundException.class,
                ()-> userService.removeUserById(0));
        assertEquals("Value doesn't exist", exception.getMessage());
    }

    @Test()
    public void removeAllUser() {
        userService.removeAllUser();
        verify(userRepository, times(1)).deleteAll();
    }

    private User createUser() {
        return new User(1, "testFirstName", "TestLastName"
                      , "test@email.com", LocalDate.now().minusYears(15));
    }

    private void setUserData(User newUser) {
        newUser.setFirstName("noExistingFirstName");
        newUser.setLastName("noExistingLastName");
        newUser.setEmail("noExisting@email");
        newUser.setBirthDate(LocalDate.now().minusYears(15));
    }
}