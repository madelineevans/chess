//package ui.websocket;
//
//import websocket.messages.ServerMessage;
//
//public class WebsocketCommunicator {
//    public void onMessage (String message){
//        try{
//            ServerMessage message = gson.fromJson(message, ServerMessage.class);
//            observer.notify(message);
//        } catch (Exception ex){
//            observer.notify(new ErrorMessage(ex.getMessage()));
//        }
//    }
//}
