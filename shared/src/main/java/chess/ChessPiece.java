package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pColor;
    private ChessPiece.PieceType pType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        pColor = pieceColor;
        pType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (board.getPiece(myPosition).getPieceType()) {
            case KING:
                return KingMoves(board, myPosition);
            case QUEEN:
                return QueenMoves(board, myPosition);
            case BISHOP:
                return BishopMoves(board, myPosition);
            case ROOK:
                return RookMoves(board, myPosition);
            case KNIGHT:
                return KnightMoves(board, myPosition);
            case PAWN:
                return PawnMoves(board, myPosition);
        }
        return new ArrayList<ChessMove>();
    }

    public ArrayList<ChessMove> KingMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        for(int i = -1; i<=1; i++) {
            for(int j = -1; j<=1; j++) {
                int checkPosRow = startRow+i;
                int checkPosCol = startCol+j;

                // break if out of bounds
                if (checkPosRow<=0||checkPosRow>=9||checkPosCol<=0||checkPosCol>=9) {
                    continue;
                }
                ChessPiece checkPiece = board.getPiece(new ChessPosition(checkPosRow, checkPosCol));
                if (board.getPiece(new ChessPosition(checkPosRow, checkPosCol)) == null||checkPiece.getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkPosRow,checkPosCol),null));
                } else if (checkPiece.getTeamColor() == pColor) {
                    continue;
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> QueenMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check left-up diagonal
        int counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow + counter;
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check right-up diagonal
        counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow + counter;
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check right-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow - counter;
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check left-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow - counter;
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Up
        counter = 1;
        while(myPosition.getRow()+counter <= 8) {
            int curRow = startRow + counter;

            if (board.getPiece(new ChessPosition(curRow, startCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Down
        counter = 1;
        while(myPosition.getRow()-counter >= 1) {
            int curRow = startRow - counter;

            if (board.getPiece(new ChessPosition(curRow, startCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Right
        counter = 1;
        while(myPosition.getColumn()+counter <= 8) {
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(startRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Left
        counter = 1;
        while(myPosition.getColumn()-counter >= 1) {
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(startRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check left-up diagonal
        int counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow + counter;
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check right-up diagonal
        counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow + counter;
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check right-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow - counter;
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check left-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow - counter;
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        return possibleMoves;
    }

    public ArrayList<ChessMove> RookMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check Up
        int counter = 1;
        while(myPosition.getRow()+counter <= 8) {
            int curRow = startRow + counter;

            if (board.getPiece(new ChessPosition(curRow, startCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Down
        counter = 1;
        while(myPosition.getRow()-counter >= 1) {
            int curRow = startRow - counter;

            if (board.getPiece(new ChessPosition(curRow, startCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Right
        counter = 1;
        while(myPosition.getColumn()+counter <= 8) {
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(startRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Left
        counter = 1;
        while(myPosition.getColumn()-counter >= 1) {
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(startRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> KnightMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        int checkRow = startRow;
        int checkCol = startCol;
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check up
        checkRow = startRow+2;
        if (checkRow<=8) {
            checkCol = startCol-1;
            if (checkCol>=1) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
            checkCol = startCol+1;
            if (checkCol<=8) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
        }
        //Check down
        checkRow = startRow-2;
        if (checkRow>=1) {
            checkCol = startCol-1;
            if (checkCol>=1) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
            checkCol = startCol+1;
            if (checkCol<=8) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
        }
        //Check Right
        checkCol = startCol+2;
        if (checkCol<=8) {
            checkRow = startRow-1;
            if (checkRow>=1) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
            checkRow = startRow+1;
            if (checkRow<=8) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
        }
        //Check Left
        checkCol = startCol-2;
        if (checkCol>=1) {
            checkRow = startRow-1;
            if (checkRow>=1) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
            checkRow = startRow+1;
            if (checkRow<=8) {
                if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null || board.getPiece(new ChessPosition(checkRow, checkCol)).getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> PawnMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        int checkRow = startRow;
        int checkCol = startCol;
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int baseOffset = 0;
        int promotionRow = 0;
        ChessPiece startPiece = board.getPiece(new ChessPosition(startRow, startCol));
        ChessPiece checkPiece = null;

        if (startPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            //If black pawn, front is down
            baseOffset = -1;
            promotionRow = 1;
        } else {
            //Else, is white pawn and front is up
            baseOffset = 1;
            promotionRow = 8;
        }

        //Check Row
        checkRow = startRow+baseOffset;
        checkCol = startCol;
        if (checkRow<1||checkRow>8) {
            return possibleMoves;
        } else {
            if (checkRow==promotionRow) {
                //Promotion Logic straight ahead
                checkCol = startCol;
                checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                if (checkPiece==null) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),PieceType.KNIGHT));
                }
                //Promotion Logic to the sides
                checkCol = startCol-1;
                if (checkCol>=1&&checkCol<=8) {
                    checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                    if (checkPiece != null) {
                        if (checkPiece.getTeamColor() != pColor) {
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.ROOK));
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.KNIGHT));
                        }
                    }
                }
                checkCol = startCol+1;
                if (checkCol>=1&&checkCol<=8) {
                    checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                    if (checkPiece != null) {
                        if (checkPiece.getTeamColor() != pColor) {
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.ROOK));
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), PieceType.KNIGHT));
                        }
                    }
                }
            } else {
                //Base Logic straight ahead
                checkCol = startCol;
                checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                if (checkPiece==null) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                    if ((this.getTeamColor()==ChessGame.TeamColor.BLACK&&startRow==7)||
                        (this.getTeamColor()==ChessGame.TeamColor.WHITE&&startRow==2)) {
                        checkRow = checkRow+baseOffset;
                        if (board.getPiece(new ChessPosition(checkRow, checkCol)) == null) {
                            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkRow,checkCol),null));
                        }
                        checkRow = startRow+baseOffset;
                    }
                }
                //Base Logic to the sides
                checkCol = startCol-1;
                if (checkCol>=1&&checkCol<=8) {
                    checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                    if (checkPiece != null) {
                        if (checkPiece.getTeamColor() != pColor) {
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), null));
                        }
                    }
                }
                checkCol = startCol+1;
                if (checkCol>=1&&checkCol<=8) {
                    checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                    if (checkPiece != null) {
                        if (checkPiece.getTeamColor() != pColor) {
                            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), null));
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pColor == that.pColor && pType == that.pType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pColor, pType);
    }
}
