package springbootsample;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final AtomicLong ids = new AtomicLong(1L);
    private final List<Todo> todos = new CopyOnWriteArrayList<>();

    public List<Todo> list() {
        return todos.stream()
                .map(Todo::copy)
                .collect(Collectors.toList());
    }

    public List<Todo> search(String search) {
        return todos.stream()
                .filter(t -> t.getTask().contains(search))
                .map(Todo::copy)
                .collect(Collectors.toList());
    }

    public Optional<Todo> get(Long id) {
        return todos.stream()
                .filter(t -> t.getId().equals(id))
                .map(Todo::copy)
                .findFirst();
    }

    public Todo create(Todo todo) {
        Long id = ids.getAndIncrement();
        todo.setId(id);
        todos.add(todo);
        return todo;
    }

    public Todo update(Todo updatedTodo) {
        Optional<Todo> match = todos.stream()
                .filter(updatedTodo::testIdMatch)
                .findFirst();
        if (match.isPresent()) {
            Todo existingTodo = match.get();
            existingTodo.setTask(updatedTodo.getTask());
            existingTodo.setComplete(updatedTodo.isComplete());
            return Todo.copy(existingTodo);
        }
        throw new ResourceDoesNotExistException("No todo found for id " + updatedTodo.getId());
    }

    public void delete(Long id) {
        Collection<Todo> matches = todos.stream()
                .filter( t -> t.getId().equals(id))
                .collect(Collectors.toSet());
        if (!matches.isEmpty()) {
            todos.removeAll(matches);
        } else {
            throw new ResourceDoesNotExistException("No todo found for id " + id);
        }
    }
}
