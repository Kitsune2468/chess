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
        String resultBody;
        try {
            dataBaseService.clear();
            result.status(200);
            resultBody = "{}";
        } catch (Error e) {
            result.status(500);
            resultBody = "{ \"message\": \"Error:"+e.getMessage()+"\" }";
        }
        return resultBody;
    }
}
