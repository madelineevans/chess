package dataaccess;
import model.Data;

public interface DataAccess {
    Data createData();
    Data readData();
    Data updateData();
    void deleteData();
}
