package model;

public sealed interface Data permits AuthData, GameData, UserData {
}
