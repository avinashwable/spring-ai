package com.avi.spring.aiworkshop.tools.act;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tools/act")
public class TaskController {

    private final ChatClient chatClient;

    public TaskController(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/tasks")
    public String performAction(@RequestParam String message) {
        return chatClient.prompt()
                .tools(new TaskManagementTools())
                .user(message)
                .call()
                .content();
        /**
         * Response without providing tools (comment .tools(...) line):
         * avinash@Admins-MacBook-Pro aiworkshop % curl "http://localhost:8080/tools/act/tasks?message=Create%20task%20with%20name%20todo"
         * Do you mean you want me to create a task named "todo" in a specific app or place? Which one should I use (examples: Todoist, Google Tasks, Apple Reminders, Microsoft To Do, Asana, Trello, Notion, GitHub issues, or just a plain note here)?
         *
         * Also tell me any of these optional details you want added:
         * - Due date/time
         * - Priority or labels
         * - Project or list
         * - Notes/description
         * - Recurrence
         *
         * If you want, I can also give the exact command or steps for the app you use, or create the task here in this chat. Which do you prefer?
         *
         * Response with tools enabled:
         * avinash@Admins-MacBook-Pro aiworkshop % curl "http://localhost:8080/tools/act/tasks?message=Create%20task%20with%20name%20todo"
         * Task created.
         *
         * - ID: 1
         * - Title: todo
         * - Status: PENDING
         * - Assignee: unassigned
         * - Details: Created by assistant; no further details provided.
         *
         * Would you like to assign this task to someone or add more details?%
         * avinash@Admins-MacBook-Pro aiworkshop %
         */
    }
}
