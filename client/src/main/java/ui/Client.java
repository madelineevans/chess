package ui;

abstract class Client {
    protected String authToken;
    protected State state = State.SIGNEDOUT;
}
