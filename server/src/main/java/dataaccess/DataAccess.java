package dataaccess;
import model.Data;
import model.UserData;

public interface DataAccess {
    Data createData();
    Data readData();
    Data updateData();
    Data deleteData();
}
