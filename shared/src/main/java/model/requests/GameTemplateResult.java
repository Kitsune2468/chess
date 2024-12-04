package model.requests;

import chess.ChessGame;

public record GameTemplateResult(int gameID,
                                 String whiteUsername,
                                 String blackUsername,
                                 String gameName,
                                 ChessGame game,
                                 boolean gameActive) {
}
