package springbootsample;

import java.util.function.Function;

public class Todo {

    private Long id;

    private String task;

    private boolean complete;

    public Todo() {
    }

    public Todo(Long id, String task, boolean complete) {
        this.id = id;
        this.task = task;
        this.complete = complete;
    }

    public static Todo copy(Todo todo) {
        return new Todo(todo.getId(), todo.getTask(), todo.isComplete());
    }

    public boolean testIdMatch(Todo todo) {
        return id != null && id.equals(todo.getId());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", complete=" + complete +
                '}';
    }
}
