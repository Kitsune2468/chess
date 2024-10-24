package service.Requests;

import chess.ChessGame;

public record JoinGameRequest(String playerColor, int gameID) {
}
