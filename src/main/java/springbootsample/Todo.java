package springbootsample;

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
}
