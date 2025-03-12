package dataaccess;
import model.AuthData;

public interface AuthDAO extends DataAccess<AuthData>{
    void deleteData(String s);
}