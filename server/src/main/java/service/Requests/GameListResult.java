package service.Requests;

import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;

public record GameListResult(ArrayList<GameTemplateResult> games) {
}
