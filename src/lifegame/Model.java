package lifegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {

    public interface NewGenerationEventListener {

        void handle(Cells newCells);
    }

    protected final int width;

    private final int length;

    protected final int[] current;

    private final int[] next;

    private final int[] xy = new int[2];

    private final List<NewGenerationEventListener> listeners = new ArrayList<>();

    private int generation = 1;

    public Model(int[] current, int width) {
        this.current = current;
        this.width = width;
        length = current.length;
        next = new int[length];
    }

    public void addNewGenerationEventListener(
            NewGenerationEventListener listener) {
        listeners.add(listener);
    }

    public Cells getCurrentGeneration() {
        return new Cells() {

            private final int[] value = Arrays.copyOf(current, length);

            private final int generation = Model.this.generation;

            @Override
            public boolean isAlive(int x, int y) {
                int index = toIndex(x, y);
                return value[index] == 1;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return length / width;
            }

            @Override
            public int getGeneration() {
                return generation;
            }
        };
    }

    public void nextGeneration() {
        generation++;
        updateNextGeneration(next);
        System.arraycopy(next, 0, current, 0, length);
        Cells nextCells = getCurrentGeneration();
        for (NewGenerationEventListener listener : listeners) {
            listener.handle(nextCells);
        }
    }

    protected int nextCell(int x, int y) {
        int sum = 0;

        for (int addY = -1; addY <= 1; addY++) {
            for (int addX = -1; addX <= 1; addX++) {
                int index = toIndex(x + addX, y + addY);
                if (-1 < index && index < length) {
                    sum += current[index];
                }
            }
        }
        int self = current[toIndex(x, y)];
        sum -= self;

        if (self == 0) {
            return sum == 3 ? 1 : 0;
        }

        return sum == 2 || sum == 3 ? 1 : 0;
    }

    protected void updateNextGeneration(int[] next) {
        for (int i = 0; i < length; i++) {
            toXY(i, xy);
            int x = xy[0];
            int y = xy[1];
            next[i] = nextCell(x, y);
        }
    }

    protected void toXY(int index, int[] xy) {
        xy[0] = index % width;
        xy[1] = index / width;
    }

    protected int toIndex(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= length / width) {
            return -1;
        }
        return x + width * y;
    }
}
