package com.example.aep;
import java.util.List;


public class ChatGPTRequest {
    //private String prompt;
    private String model;
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }



    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public static class Message {
        private String role;    // "system", "user", etc.
        private String content; // The actual message content

        // Constructor, getters and setters


        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
