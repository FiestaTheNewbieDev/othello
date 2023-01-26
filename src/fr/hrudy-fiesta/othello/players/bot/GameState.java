package fr.hrudyfiesta.othello.players.bot;

import fr.hrudyfiesta.othello.Game;
import fr.hrudyfiesta.othello.utils.Coords;

import java.util.ArrayList;

public class GameState {
    // Plateau
    private int[][] board;

    // Joueur actuel, 1 = simulation du joueur, 2 = simulation de l'ordinateur
    private int currentPlayer = 2;

    // profondeur max
    private int maxDepth = 5;
    // profondeur du GameState
    private int depth = 0;

    // Score de la partie, si négatif l'ordinateur perd sinon l'ordinateur gagne
    private int score = 0;

    // Coordonnées qui vienne d'être "jouées".
    private int x = 0;
    private int y = 0;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

    // Liste des GameState enfants
    public ArrayList<GameState> children = new ArrayList<>();

    /**
     Initialise le gamestate racine en copiant la partie en cours.

     @param x Coordonnée X du coup possible (assumée vraie)
     @param y Coordonnée Y du coup possible (assumée vraie)
     @param maxDepth Profondeur de recherche maximum
    */
    public GameState(int x, int y, int maxDepth) {
        this.maxDepth = maxDepth;
        this.depth = 0;

        this.board = new int[Game.getBoardSize()][Game.getBoardSize()];
        for(int i = 0; i < this.board.length; i += 1) {
            for (int j = 0; j < this.board.length; j += 1) this.board[i][j] = Game.getPawn(i, j);
        }

        this.x = x;
        this.y = y;

        this.currentPlayer = 2;

        this.updateBoard(x, y);

        this.updateChildren();

        this.updateScore();
    }

    /**
     Initialise un enfant de GameState

     @param gameState Le GameState parent
     @param x Coordonnée X du coup choisi (assumée vraie)
     @param y Coordonnée Y du coup possible (assumée vraie)
    */
    private GameState(GameState gameState, int x, int y) {
        this.maxDepth = gameState.maxDepth;
        this.depth = gameState.depth + 1;

        this.board = new int[gameState.board.length][gameState.board.length];
        for(int i = 0; i < this.board.length; i = i + 1) {
            for (int j = 0; j < this.board.length; j = j + 1) this.board[i][j] = gameState.board[i][j];
        }

        this.x = x;
        this.y = y;

        if (gameState.currentPlayer == 1) this.currentPlayer = 2;
        else this.currentPlayer = 1;

        this.updateBoard(x, y);

        this.updateChildren();

        this.updateScore();
    }

    /**
     Mets à jour la liste des enfants avec les coups possibles à ce tour
    */
    private void updateChildren() {
        Coords[] possibleMoves = getPossibleMoves();
        if (this.depth < this.maxDepth && possibleMoves.length > 0) {
            for (int i = 0; i < possibleMoves.length; i = i + 1) this.children.add(new GameState(this,  possibleMoves[i].x, possibleMoves[i].y));
        }
    }

    /**
     @return La liste des coordonnées de coups possibles à ce tour
    */
    private Coords[] getPossibleMoves() {
        ArrayList<Coords> list = new ArrayList<>();

        // Vérifie toutes les cases du tableau pour les coups possibles
        for (int x = 0; x < this.board.length; x += 1) {
            for (int y = 0; y < this.board.length; y += 1) {
                if (isValidMove(x, y)) list.add(new Coords(x, y));
            }
        }
        return list.toArray(new Coords[0]);
    }

    /**
     @param x Coordonnée X à vérifier
     @param y Coordonnée Y à vérifier
     @return Si le coup aux coordonnées données est possible
    */
    private boolean isValidMove(int x, int y) {
        // Si la case est déjà occupée, le coup ne peut pas être valide.
        if (this.board[x][y] != 0) return false;

        // Si le coup est en dehors des limites du tableau, le coup n'est pas possible.
        if (x < 0 || y < 0 || x >= this.board.length || y >= this.board.length) return false;

        // Vérifie pour chaque direction si le coup remplis toutes les conditions (haut, bas, gauche, droite, diagonales)
        for (int dx = -1; dx <= 1; dx += 1) {
            for (int dy = -1; dy <= 1; dy += 1) {
                if (dx == 0 && dy == 0) continue;  // Ignore la case actuelle.
                if (checkDirection(x, y, dx, dy)) return true;  // Un coup valide existe dans une direction.
            }
        }

        return false; // Le coup n'est pas valide.
    }

    /**
     Mets à jour le tableau du GameState avec les coordonnées du coup joué.

     @param x Coordonnée X jouée (assumée vraie).
     @param y Coordonnée Y jouée (assumée vraie).
    */
    private void updateBoard(int x, int y) {
        // Pour toutes les directions possibles,
        for (int dx = -1; dx <= 1; dx += 1) {
            for (int dy = -1; dy <= 1; dy += 1) {
                // Si la direction en question est valide,
                if (checkDirection(x, y, dx, dy)) {
                    // On sauvegarde la position actuelle.
                    int originX = x;
                    int originY = y;

                    // On change toutes les cases qui ne sont pas du joueur dans cette direction.
                    while (x >= 0 && y >= 0 && x < this.board.length && y < this.board.length && ((x == originX && y == originY) || this.board[x][y] != this.currentPlayer)) {

                        this.board[x][y] = this.currentPlayer;

                        // On passe à la prochaine case de la direction.
                        x += dx;
                        y += dy;
                    }

                    // On restaure la position de base, pour les autres directions.
                    x = originX;
                    y = originY;
                }
            }
        }
    }

    /**
     Vérifie si le coup donné permet de valider les autres conditions d'un coup valide dans une direction donnée

     @param x Coordonnée X à vérifier
     @param y Coordonnée Y à vérifier
     @param dx Direction en X
     @param dy Direction en Y
     @return Si le coup encadre et un pion adverse correctement
    */
    private boolean checkDirection(int x, int y, int dx, int dy) {
        // Récupère l'id de l'opposant.
        int opponent = this.currentPlayer == 1? 2 : 1;

        // Compteur de pions valides.
        int count = 0;

        // On avance dans la direction.
        x += dx;
        y += dy;

        // Tant que l'on n'atteint pas la limite du tableau et qu'on n'a pas rencontré une case du joueur actuel.
        while (x >= 0 && y >= 0 && x < this.board.length && y < this.board.length && this.board[x][y] == opponent) {
            count += 1;
            x += dx;
            y += dy;
        }

        // Si les coordonnées actuelles sont toujours valide, que l'on a rencontré au moins une case qui n'est pas du joueur actuel,
        // et que l'on termine sur une case du joueur, cette direction sera modifiée par la suite et le coup est valide.
        return x >= 0 && y >= 0 && x < this.board.length && y < this.board.length && count > 0 && this.board[x][y] == this.currentPlayer;
    }

    /**
     Renvoit la liste des pions actuellement dans le tableau d'un joueur donné.

     @param player Joueur à compter
     @return Le nombre de pions du joueur
    */
    private int getPlayerScore(int player) {
        int count = 0;
        for(int y = 0; y < this.board.length; y = y + 1) {
            for (int x = 0; x < this.board.length; x = x + 1) {
                if (this.board[x][y] == player) {
                    count += 1;
                }
            }
        }
        return count;
    }

    /**
     Mets à jour le score du GameState.
     Agit concrètement comme l'heuristique ici.
    */
    private void updateScore() {
        if (this.children.size() == 0) this.score = this.getPlayerScore(2) - this.getPlayerScore(1);
        else {
            this.score = 0;
            for (GameState s : this.children) this.score += s.score;
        }
    }

    /**
     Dessine le noeud
    */
    public void draw() {
        String result = "";
        for (int i = 0; i < depth; i += 1) result += "   ";

        result += "|_ x: " + this.x + " y: " + this.y + " score: " + this.score;

        System.out.println(result);

        for (GameState s : this.children) s.draw();
    }
}
