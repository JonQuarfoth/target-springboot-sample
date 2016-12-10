package springbootsample;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {

    private final AtomicLong ids = new AtomicLong(1L);
    private final Map<Long, Todo> todos = new HashMap<>();

    public List<Todo> list() {
        return new ArrayList<>(todos.values());
    }

    public Optional<Todo> get(Long id) {
        Todo todo = todos.get(id);
        if (todo != null) {
            return Optional.of(new Todo(todo.getId(), todo.getTask(), todo.isComplete()));
        }
        return Optional.empty();
    }

    public Todo create(Todo todo) {
        Long id = ids.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    public Todo update(Todo todo) {
        Long id = todo.getId();
        Optional<Todo> previous = get(id);
        if (previous.isPresent()) {
            todo.setTask(todo.getTask());
            todo.setComplete(todo.isComplete());
            todos.put(todo.getId(), todo);
            return todo;
        }
        throw new ResourceDoesNotExistException("No todo found for id " + id);
    }

    public Todo delete(Long id) {
        if (todos.containsKey(id)) {
            return todos.remove(id);
        }
        throw new ResourceDoesNotExistException("No todo found for id " + id);
    }
}
