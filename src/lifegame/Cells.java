package lifegame;

public interface Cells {

    boolean isAlive(int x, int y);

    int getWidth();

    int getHeight();

    int getGeneration();
}
