package tiy.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by erronius on 12/21/2016.
 */

@Controller
public class TodoAppController {

    public static final String FILE_EXT = "_todo.json";

    ToDoList todoList;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, HttpSession session) {
        model.addAttribute("name", session.getAttribute("userName"));
        if ((todoList == null) && (session.getAttribute("userName") != null)) {
            try {
                String jsonString = readFromFile((String)session.getAttribute("userName") + FILE_EXT);
                System.out.println(jsonString);
                todoList = ToDoList.jsonRestore(jsonString);
                System.out.println(todoList);
            } catch (FileNotFoundException ex) {
                todoList = new ToDoList();
            }
        }
        model.addAttribute("todolist", todoList);
        return "index";
    }

    @RequestMapping(path = "/todolist", method = RequestMethod.GET)
    public String todoList(HttpSession session, Model model) {
        //String userName = (String) session.getAttribute("userName");

        model.addAttribute("todo-list", todoList.getTodoList());
        return "todolist";
    }

    @RequestMapping(path = "/newtodo", method = RequestMethod.GET)
    public String newTodo(HttpSession session, String todoText) {
        return "newtodo";
    }

    @RequestMapping(path = "/additem", method = RequestMethod.POST)
    public String addTodo(String todoText) {
        todoList.add(new ToDoItem(todoText));
        return "redirect:/";
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String userName) {
        session.setAttribute("userName", userName);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        String userName = (String)session.getAttribute("userName");
        session.removeAttribute("userName");
        if (todoList != null) {
            writeToFile(userName + FILE_EXT, todoList.jsonSave());
        }
        todoList = null;
        return "redirect:/";
    }

    @RequestMapping(path = "/checkoff", method = RequestMethod.POST)
    public String checkoff(HttpSession session, String userSelection){
        int response = Integer.parseInt(userSelection);
        response--;
        todoList.getTodoList().get(response).setDone(true);
        return "redirect:/";
    }

    public void writeToFile (String fileName, String contents) {
        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(contents);
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String readFromFile (String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner fileScanner = new Scanner(file);
        if (fileScanner.hasNext()) {
            return fileScanner.nextLine();
        }
        return null;
    }

}
