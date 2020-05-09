package org.example.model;

public class ConvertResponse {
    private int status;
    private String server;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public ConvertResponse(int status, String server, String content) {
        this.status = status;
        this.server = server;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
