package ru.javawebinar.topjava.web.user;

import com.sun.xml.bind.v2.TODO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> createOrUpdate(@Valid UserTo userTo, BindingResult result) {
        System.out.println("createOrUpdate");
        if (result.hasErrors()) {
//            System.out.println("result " + result);
//            result.getFieldErrors().stream()
//                    .forEach(fe -> {
//                        System.out.printf("[%s] %s", fe.getField(), fe.getDefaultMessage());
//                        System.out.println();
//                    });
//            System.out.println(result.getFieldErrors().get(1).);
//            return new ResponseEntity<>("EEEEEEE", HttpStatus.NO_CONTENT);
//             TODO change to exception handler
            ResponseEntity<String> errorResponse = ValidationUtil.getErrorResponse(result);
//            System.out.println(errorResponse.getBody());
//            return new ResponseEntity<>(errorResponse.getBody(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
            throw new IllegalRequestDataException(errorResponse.getBody());
//            return errorResponse;
        }
        if (userTo.isNew()) {
            super.create(userTo);
        } else {
            super.update(userTo, userTo.id());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }
}
