package com.example.todo_app.Controller;

import com.example.todo_app.Model.Task;
import com.example.todo_app.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String getAllTasks(Model model, @RequestParam(defaultValue = "all") String filter) {
        List<Task> tasks = taskService.getAllTasks();
        if ("completed".equals(filter)) {
            tasks = tasks.stream().filter(Task::isCompleted).collect(Collectors.toList());
        } else if ("incomplete".equals(filter)) {
            tasks = tasks.stream().filter(task -> !task.isCompleted()).collect(Collectors.toList());
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());
        model.addAttribute("filter", filter);
        return "index";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, @ModelAttribute Task taskDetails) {
        taskService.updateTask(id, taskDetails);
        return "redirect:/tasks";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTaskCompletion(@PathVariable Long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(!task.isCompleted());
        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}