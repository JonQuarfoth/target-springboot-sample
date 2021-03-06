package springbootsample

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class TodoControllerSpec extends Specification {

    private TodoService todoService
    private TodoController todoController

    private Todo todo1, todo2, todo3
    private List<Todo> defaultTodoList

    def setup() {
        todoService = Mock(TodoService)
        todoController = new TodoController(todoService)

        todo1 = new Todo(1L, "Write Tests", true)
        todo2 = new Todo(2L, "???", false)
        todo3 = new Todo(3L, "Profit", false)

        defaultTodoList = [todo1, todo2, todo3]
    }

    def "should call through to list on list() without query"() {

        when:
        List<Todo> todos = todoController.list(Optional.empty())

        then:
        1 * todoService.list() >> defaultTodoList
        todos == defaultTodoList
    }

    def "should call through to search() on list() with query"() {
        given:
        String query = 'search query'

        when:
        List<Todo> todos = todoController.list(Optional.of(query))

        then:
        1 * todoService.search(query) >> defaultTodoList
        todos == defaultTodoList
    }

    def "should get create new todo on create()"() {

        given:
        Todo inputTodo = new Todo(task: 'write a unit test', complete: true)
        Todo outputTodo = new Todo(id: 1L, task: inputTodo.task, complete: inputTodo.complete)

        when:
        ResponseEntity result = todoController.create(inputTodo)

        then:
        1 * todoService.create(inputTodo) >> outputTodo
        result.statusCode == HttpStatus.OK
        result.body == outputTodo

    }

    def "should respond with 400 if a new Todo is posted with an id"() {

        given:
        Todo todo = new Todo(id: 1L, task: 'read a book', complete: false)

        when:
        ResponseEntity result = todoController.create(todo)

        then:
        0 * todoService.create(_)
        result.statusCode == HttpStatus.BAD_REQUEST
        result.body instanceof String
    }

    def "controller update() should send todo to service update() "() {

        given:
        Todo todoIn = new Todo(id: 1L)
        Todo todoOut = new Todo(id: 1L)

        when:
        ResponseEntity result = todoController.update(todoIn, Optional.of(todoIn.id))

        then:
        1 * todoService.update(todoIn) >> todoOut
        result.statusCode == HttpStatus.OK
        result.body == todoOut
    }

    def "controller update() should send todo to service update() and return 404 on ResourceDoesNotExistException"() {

        given:
        Todo todo = new Todo(id: 1L)
        String errorMessage = 'error message!'

        when:
        ResponseEntity result = todoController.update(todo, Optional.of(todo.id))

        then:
        1 * todoService.update(todo) >> { throw new ResourceDoesNotExistException(errorMessage) }
        result.statusCode == HttpStatus.NOT_FOUND
        result.body == errorMessage
    }

    def "controller update() should use path id if no todo id is set"() {

        given:
        Todo todo = new Todo()

        when:
        ResponseEntity result = todoController.update(todo, Optional.of(1L))

        then:
        todo.id == 1L

        and:
        1 * todoService.update(todo)
        result.statusCode == HttpStatus.OK
    }

    def "controller update() should use todo id if no path id is provided"() {

        given:
        Todo todo = new Todo(id: 1L)

        when:
        ResponseEntity result = todoController.update(todo, Optional.empty())

        then:
        todo.id == 1L

        and:
        1 * todoService.update(todo)
        result.statusCode == HttpStatus.OK
    }

    def "controller update() should error if path id and body id do not match"() {

        given:
        Todo todo = new Todo(id: 1L)

        when:
        ResponseEntity result = todoController.update(todo, Optional.of(2L))

        then:
        0 * todoService.update(todo)
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "controller update() should error if no path id or todo id is provided"() {

        given:
        Todo todo = new Todo()

        when:
        ResponseEntity result = todoController.update(todo, Optional.empty())

        then:
        0 * todoService.update(todo)
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "get() should return todo from service"() {

        given:
        Todo todo = new Todo(id: 1L, task: 'follow the leader', complete: false)

        when:
        ResponseEntity result = todoController.get(1L)

        then:
        1 * todoService.get(todo.id) >> Optional.of(todo)
        result.statusCode == HttpStatus.OK
        result.body == todo
    }

    def "get() should return 404 if id is not found"() {

        when:
        ResponseEntity result = todoController.get(1L)

        then:
        1 * todoService.get(1L) >> Optional.empty()
        result.statusCode == HttpStatus.NOT_FOUND
    }

    def "delete() should call through to service"() {
        when:
        ResponseEntity result = todoController.delete(1L)

        then:
        1 * todoService.delete(1L)
        result.statusCode == HttpStatus.OK
    }

    def "delete() should fail when id is not found"() {
        given:
        String errorMessage = 'error'

        when:
        ResponseEntity result = todoController.delete(1L)

        then:
        1 * todoService.delete(1L) >> { throw new ResourceDoesNotExistException(errorMessage)}
        result.statusCode == HttpStatus.NOT_FOUND
        result.body == errorMessage
    }

}
