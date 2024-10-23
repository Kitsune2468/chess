package handlers;

import dataaccess.DataAccessException;
import service.DataBaseService;
import spark.Request;
import spark.Response;

public class DataBaseHandler {
    DataBaseService dataBaseService;
    public DataBaseHandler(DataBaseService inputDataBaseService) {
        dataBaseService = inputDataBaseService;
    }

    public Response clear(Request request, Response result) throws DataAccessException {
        try {
            dataBaseService.clear();
            result = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
