import chess.*;
import client.ServerFacade;
import server.Server;
import ui.PreLoginUI;

public class Main {

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        Server testServer = new Server();
        testServer.run(8080);
        ServerFacade testFacade = new ServerFacade(8080);
        PreLoginUI preLoginUI = new PreLoginUI(testFacade);

        preLoginUI.run();
    }
}