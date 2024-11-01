package service.requests;

public record GameTemplateResult(int gameID,
                                 String whiteUsername,
                                 String blackUsername,
                                 String gameName) {
}