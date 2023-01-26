package fr.hrudyfiesta.othello.players.bot;

public enum BotDifficulty {
    EASY(0, "Easy"),
    MEDIUM(1, "Medium"),
    HARD(2, "Hard");

    private final int id;
    private final String string;

    BotDifficulty(int id, String string) {
        this.id = id;
        this.string = string;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
