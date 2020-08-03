package westernacher.solution.task.resource;
import westernacher.solution.task.dao.models.User;
import westernacher.solution.task.resource.models.UserDTO;
import westernacher.solution.task.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
           this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserDTO>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@RequestBody User user,
                                                       @PathVariable("id") int id) throws Exception {
        userService.updateUser(user, id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") int id) {
        userService.removeUserById(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAllUser() {
        userService.removeAllUser();

        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) throws NullPointerException {

         return userService.addUser(user).map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());

    }
}