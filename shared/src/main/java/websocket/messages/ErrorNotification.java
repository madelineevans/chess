package websocket.messages;

public class ErrorNotification extends ServerMessage{
    private final String errorMessage;
    public ErrorNotification(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
