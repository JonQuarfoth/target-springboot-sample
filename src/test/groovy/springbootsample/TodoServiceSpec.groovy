package springbootsample;

import spock.lang.Specification
import spock.lang.Unroll;

class TodoServiceSpec extends Specification {

    private TodoService todoService

    def setup() {
        todoService = new TodoService()
    }

    def "create() should create a new Todo"() {
        given:
        String task = 'create a todo list'
        boolean complete = false

        when:
        Todo todo = todoService.create(new Todo(task: task, complete: complete))

        then:
        todo.id == 1L
        todo.task == task
        todo.complete == complete
    }

    def "create() should create a new Todos with unique ids"() {
        given:
        List<Todo> tasks = [
                new Todo(task: 'do thing 1', complete: true),
                new Todo(task: 'do thing 2', complete: false),
                new Todo(task: 'do thing 3', complete: true),
                new Todo(task: 'do thing 4', complete: false),
                new Todo(task: 'do thing 5', complete: true),
        ]

        when:
        List<Todo> results = tasks.collect {
            todoService.create(it)
        }

        then:
        results*.id == [1L, 2L, 3L, 4L, 5L]
    }

    def "get() should return a todo by id"() {
        given:
        Todo todo = new Todo(id: 1L, task: 'watch every Star Wars Movie', complete: true)
        todoService.create(todo)

        expect:
        Todo result = todoService.get(todo.id).get()
        result.id == todo.id
        result.task == todo.task
        result.complete == todo.complete
    }

    def "get() should return nothing if no id matches"() {
        expect:
        todoService.get(1L) == Optional.empty()
    }

    @Unroll
    def "list() should return #n todos"() {
        given:
        List<Todo> todos = []
        n.times {
            todoService.create(new Todo(task: "do thing $n", complete: false))
        }

        expect:
        todoService.list().size() == n

        where:
        n << [0, 1, 2, 3, 10, 100]
    }

    def "update() updates the Todo"() {
        given:
        Todo todo = new Todo(task: 'find time travel device', complete: false)
        todo = todoService.create(todo)
        Todo updateTodo = new Todo(id: todo.id, task: todo.task, complete: true)

        when:
        Todo updateResult = todoService.update(updateTodo)

        and:
        Todo getResult = todoService.get(updateResult.id).get()

        then:
        !todo.isComplete()
        updateResult.isComplete()
        getResult.isComplete()
    }

    def "update() throws exception if there is no matching Todo"() {
        given:
        Todo todo = new Todo(id: 1L, task: 'find time travel device', complete: false)

        when:
        todoService.update(todo)

        then:
        ResourceDoesNotExistException ex = thrown()
        ex.message != null
    }

    def "delete() should delete the todo"() {
        given:
        Todo todo = new Todo(id: 1L, task: 'find time travel device', complete: false)
        todoService.create(todo)

        when:
        todoService.delete(todo.id)

        then:
        !todoService.get(todo.id).isPresent()
    }

    def "delete() should throw an exception if there is no matching Todo"() {
        when:
        todoService.delete(1L)

        then:
        thrown(ResourceDoesNotExistException)
    }
}