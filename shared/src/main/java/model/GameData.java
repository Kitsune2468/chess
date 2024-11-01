package model;
import chess.*;

public record GameData(int gameID,
                       String gameName,
                       String whiteUsername,
                       String blackUsername,
                       ChessGame game) {
}
