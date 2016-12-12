package springbootsample

import spock.lang.Specification
import spock.lang.Unroll

class TodoSpec extends Specification {

    @Unroll
    def "Copy method should result in an equal todo object [id: #id, task: #task, complete: #complete]"() {
        given:
        Todo source = new Todo(id, task, complete)

        when:
        Todo result = Todo.copy(source)

        then:
        !source.is(result)
        source.id == result.id
        source.task == result.task
        source.complete == result.complete

        where:
        id | task             | complete
        1L | 'watch a movie'  | false
        2L | 'read a book'    | true
        3L | 'draw a picture' | false
    }

    @Unroll
    def "testIdMatch #id == #otherId should return #result"() {
        given:
        Todo source = new Todo(id: id)
        Todo other = new Todo(id: otherId)

        expect:
        result == source.testIdMatch(other)

        where:
        id   | otherId || result
        0L   | 0L      || true
        1L   | 1L      || true
        1L   | 2L      || false
        2L   | 1L      || false
        null | null    || false
        1L   | null    || false
        null | 1L      || false
    }
}