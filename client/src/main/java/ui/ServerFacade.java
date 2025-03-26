package ui;
import com.google.gson.Gson;
import exceptions.BadRequest;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import requests.*;
import results.*;
import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, LogoutResult.class, request.authToken());
    }

    public ListResult listGames(ListRequest request) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListResult.class, request.authToken());
    }

    public CreateResult createGames(CreateRequest request) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateResult.class, request.authToken());
    }

    public JoinResult joinGame(JoinRequest request) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("PUT", path, request, JoinResult.class, request.authToken());
    }

    public void clear() throws DataAccessException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, Void.class, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws DataAccessException {
        try {
            //tells it the endpoint
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            if(authToken!=null){
                http.addRequestProperty("authorization", authToken);  //this adds the header
            }
            http.setDoOutput(true); //okay now we write out data
            writeBody(request, http);

            http.connect(); //actually makes the request
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new BadRequest("Error: BadRequest" +ResponseException.fromJson(respErr).getMessage());
                }
            }

            throw new DataAccessException("HTTP error: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
//        if (http.getContentLength() < 0) {
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(respBody);
            if (responseClass != null && responseClass != Void.class) {
                response = new Gson().fromJson(reader, responseClass);
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
