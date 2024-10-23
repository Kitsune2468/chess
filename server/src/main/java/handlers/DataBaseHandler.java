package handlers;

import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import service.DataBaseService;
import spark.Request;
import spark.Response;

public class DataBaseHandler {
    DataBaseService dataBaseService;
    public DataBaseHandler(DataBaseService inputDataBaseService) {
        dataBaseService = inputDataBaseService;
    }

    public Object clear(Request request, Response result) throws DataAccessException {
        String body;
        try {
            dataBaseService.clear();
            result.status(200);
            body = "{}";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return body;
    }
}
