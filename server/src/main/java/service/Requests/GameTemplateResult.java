package service.Requests;

public record GameTemplateResult(int gameID,
                                 String whiteUsername,
                                 String blackUsername,
                                 String gameName) {
}
