package tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tiy.webapp.ToDoItem;
import tiy.webapp.ToDoList;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Created by erronius on 12/19/2016.
 */
public class TestJsonSerialization {

    ToDoList todoList;
    @Before
    public void setUp() throws Exception {
        todoList = new ToDoList();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSerialization() {
        todoList.add(new ToDoItem("Apples"));
        todoList.add(new ToDoItem("Bananas", true));
        todoList.add(new ToDoItem("Oranges"));

        String jsonString = todoList.jsonSave();
        System.out.println(jsonString);
        ToDoList restoredList = ToDoList.jsonRestore(jsonString);
        System.out.println(restoredList);
        assertEquals("Apples", restoredList.getTodoList().get(0).getText());
        assertEquals(true, restoredList.getTodoList().get(1).isDone());
        assertEquals(false, restoredList.getTodoList().get(2).isDone());
    }

    @Test
    public void testSerializationFromFile() throws FileNotFoundException {
        String jsonString = new TodoAppController().readFromFile("../Paul_todo.json");
        todoList = ToDoList.jsonRestore(jsonString);
        System.out.println(todoList);
    }

}