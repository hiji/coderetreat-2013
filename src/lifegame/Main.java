package lifegame;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import lifegame.Model.NewGenerationEventListener;
import lifegame.View.NextGenerationEventListener;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int width = 120;
                int height = 90;
                int[] initial = new int[width * height];
                Random r = new Random();
                for (int i = 0; i < initial.length; i++) {
                    initial[i] = r.nextInt(2);
                }
                final Model model = new Model(initial, width);

                Cells cells = model.getCurrentGeneration();
                final View view = new View();
                view.update(cells);

                model.addNewGenerationEventListener(new NewGenerationEventListener() {

                    @Override
                    public void handle(Cells newCells) {
                        view.update(newCells);
                    }
                });
                view.addNextGenerationEventListener(new NextGenerationEventListener() {

                    @Override
                    public void handle() {
                        model.nextGeneration();
                    }
                });

                view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                view.pack();
                view.setLocationRelativeTo(null);
                view.setVisible(true);
                view.start();
            }
        });
    }
}
