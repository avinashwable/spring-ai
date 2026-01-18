package com.avi.spring.aiworkshop.tools.get;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**********************************************************************************************************************
 * When we want to use a Tool or make a tool available to the AI model to call
 *   1 Chat Request with Tool Definition is sent to the AI Model
 *      - Tool Definition includes tool name, description, and input schema
 *      - Description is important - AI Model understands the tool capabilities from its description
 *   2 AI Model decides to call the tool based on the Tool Definition and generates a tool-call request
 *   3 Spring AI library helps to form a tool-call request and dispatches it to the appropriate Tool implementation
 *   4 Tool processes the request and generates a result
 *   5 Spring AI library takes the Tool result and sends it back to the AI Model as part of the conversation
 *   6 AI Model generates the final Chat Response incorporating the response from the Tool
 * <p>
 * Spring AI – Tool Calling Flow
 *                                     ┌──────────────────┐
 *                                     │      Tool        │
 *                                     │     ┌--------┐   │
 *                                     └─────▲────────┼───┘
 *                                           │ 3      │
 *                                           │        │
 *                           ┌───────────────┼────────┼───────────────┐
 *        ┌──────────────┐   │   Spring AI   │        │               │
 *        │ Chat Request │   │               │        │4              │
 *        │──────────────│   │         ┌──────────────▼─────┐         │   6 ┌───────────────┐
 *        │ Tool Def     │───┼───┐     │   Dispatch Tool    │     ┌───┼────►│ Chat Response │
 *        │ - name       │   │   │     │   Call Requests    │     │   │     └───────────────┘
 *        │ - desc       │   │   │     └─────▲────────┬─────┘     │   │
 *        │ - in schema  │   │   │           │ 2      │           │   │
 *        └──────────────┘   └───┼───────────┼────────┼───────────┼───┘
 *                               │           │        │           │
 *                               │ 1         │        │5          │
 *                           ┌───▼───────────┼────────▼───────────┼───┐
 *                           │   └-----------┘        └-----------┘   │
 *                           │                AI Model                │
 *                           └────────────────────────────────────────┘
 *
 *********************************************************************************************************************/
@RestController
@RequestMapping("/tools/get")
public class DateTimeController {

    private final ChatClient chatClient;

    public DateTimeController(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/now")
    public String getCurrentTime() {
        return chatClient.prompt("What day is tomorrow?")
                .tools(new DateTimeTools())
                .call()
                .content();
    }
}
