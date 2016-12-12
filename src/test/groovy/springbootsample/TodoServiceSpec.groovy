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
        Optional<Todo> getResult = todoService.get(updateResult.id)

        then:
        updateResult.isComplete()

        getResult.isPresent()
        getResult.get().isComplete()
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
        Todo todo = new Todo(task: 'find time travel device', complete: false)

        when:
        todo = todoService.create(todo)

        then:
        todoService.get(todo.id).isPresent()

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

    @Unroll
    def "search() for '#searchString' finds #resultCount results"() {
        given:
        todoService.create(new Todo(task: 'recruit evil henchman', complete: true))
        todoService.create(new Todo(task: 'find suitable site for evil volcano lair', complete: true))
        todoService.create(new Todo(task: 'build evil volcano lair ', complete: true))
        todoService.create(new Todo(task: 'capture secret agent before he foils plans', complete: true))
        todoService.create(new Todo(task: 'reveal plans to secret agent', complete: true))
        todoService.create(new Todo(task: 'ensure secret agent does not escape', complete: false))
        todoService.create(new Todo(task: 'conquer planet', complete: false))

        when:
        List<Todo> results = todoService.search(searchString)

        then:
        results.size() == resultCount

        where:
        searchString   || resultCount
        'evil'         || 3
        'good'         || 0
        'henchman'     || 1
        'volcano lair' || 2
        'secret agent' || 3

    }
}