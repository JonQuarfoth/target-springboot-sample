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
        return Optional.ofNullable(todos.get(id));
    }

    public Todo create(Todo todo) {
        Long id = ids.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    public Todo update(Todo todo) {
        Long id = todo.getId();
        Todo previous = todos.get(id);
        if (previous != null) {
            todos.put(id, todo);
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
