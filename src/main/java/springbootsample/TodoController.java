package springbootsample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TodoController {

    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/todos")
    public List<Todo> list(@RequestParam(required = false) Optional<String> query) {
        if (query.isPresent()) {
            return todoService.search(query.get().trim());
        }
        return todoService.list();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/todos")
    public ResponseEntity create(@RequestBody Todo todo) {
        if (todo.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id cannot be set on create");
        }
        return ResponseEntity.ok(todoService.create(todo));
    }

    @RequestMapping(method = RequestMethod.PUT, value = {"/todos", "/todos/{id}"})
    public ResponseEntity update(@RequestBody Todo todo, @PathVariable(name = "id") Optional<Long> pathId) {

        if (pathId.isPresent()) {
            Long id = pathId.get();
            if (todo.getId() != null && !todo.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("todo id must match path id if both are set");
            }
            todo.setId(id);
        } else if (todo.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id must be specified");
        }

        try {
            Todo updated = todoService.update(todo);
            return ResponseEntity.ok(updated);
        } catch (ResourceDoesNotExistException rdnee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rdnee.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/todos/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Todo> todo = todoService.get(id);
        if (todo.isPresent()) {
            return ResponseEntity.ok(todo.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no todo found for id " + id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/todos/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            todoService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResourceDoesNotExistException rdnee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rdnee.getMessage());
        }
    }
}