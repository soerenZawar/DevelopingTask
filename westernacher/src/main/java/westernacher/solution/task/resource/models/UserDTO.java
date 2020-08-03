package westernacher.solution.task.resource.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
}
