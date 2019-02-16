package lifegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class View extends JFrame {

    public interface NextGenerationEventListener {

        void handle();
    }

    class NextGeneration extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            return null;
        }

        @Override
        protected void done() {
            for (NextGenerationEventListener listener : listeners) {
                listener.handle();
            }
            NextGeneration task = new NextGeneration();
            long delay = 100;
            TimeUnit unit = TimeUnit.MILLISECONDS;

            //シャットダウン要求後にタスクを放り込まないようロックで制御する
            lock.lock();
            try {
                if (executor.isShutdown() == false) {
                    executor.schedule(task, delay, unit);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    class LifegamePanel extends JComponent {

        private Cells cells;

        private int size = 8;

        public void update(Cells cells) {
            this.cells = cells;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (cells == null) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                //背景をとりあえず全部塗りつぶす
                g2.setColor(Color.WHITE);
                Rectangle rect = g2.getClipBounds();
                g2.fillRect(0, 0, (int) rect.getWidth(), (int) rect.getHeight());

                g2.setColor(Color.BLACK);

                //アンチエイリアス効かせる
                Map<RenderingHints.Key, Object> hints = new HashMap<>();
                hints.put(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.addRenderingHints(hints);

                for (int x = 0; x < cells.getWidth(); x++) {
                    for (int y = 0; y < cells.getHeight(); y++) {

                        //生存しているセルを描画する
                        if (cells.isAlive(x, y)) {
                            g2.fillOval(x * size, y * size, size, size);
                        }

                    }
                }
            } finally {
                g2.dispose();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            if (cells == null) {
                return super.getPreferredSize();
            }
            return new Dimension(cells.getWidth() * size, cells.getHeight()
                    * size);
        }
    }

    //TODO 時間の流れはViewが管理するのか？　何か違う気がする……　誰か教えてちょ
    private final ScheduledExecutorService executor = Executors
            .newSingleThreadScheduledExecutor();

    private final List<NextGenerationEventListener> listeners = new ArrayList<>();

    /**
     * 現在の世代を表示する
     */
    private JLabel generation;

    private LifegamePanel lifegame;

    private final Lock lock = new ReentrantLock();

    public View() {
        setTitle("Conway's Game of Life");
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                shutdownExecutor();
            }
        });

        generation = new JLabel();
        add(generation, BorderLayout.NORTH);

        lifegame = new LifegamePanel();
        add(lifegame, BorderLayout.CENTER);
    }

    private void shutdownExecutor() {

        //新たなタスクが放り込まれる事を防ぐためロックする
        lock.lock();
        try {
            executor.shutdown();
        } finally {
            lock.unlock();
        }

        long timeout = 3;
        TimeUnit unit = TimeUnit.SECONDS;
        try {
            if (executor.awaitTermination(timeout, unit) == false) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addNextGenerationEventListener(
            NextGenerationEventListener listener) {
        listeners.add(listener);
    }

    public void start() {
        NextGeneration task = new NextGeneration();
        executor.submit(task);
    }

    public void update(Cells cells) {
        generation.setText(String.format("Generation: %d",
                cells.getGeneration()));
        lifegame.update(cells);
    }
}
