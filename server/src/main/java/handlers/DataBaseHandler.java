package handlers;

import dataaccess.DataAccessException;
import service.DataBaseService;
import spark.Request;
import spark.Response;

public class DataBaseHandler {
    public Response clear(Request request, Response result) throws DataAccessException {
        try {
            DataBaseService.clear();
            result = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
