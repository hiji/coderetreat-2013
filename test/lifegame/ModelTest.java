package lifegame;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import lifegame.Model.NewGenerationEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class ModelTest {

    public static class UpdateNextGenerationTest {

        @Test
        public void すべて生存している場合() throws Exception {
            int[] current = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
            int width = 3;
            Model model = new Model(current, width);

            int[] next = new int[9];
            model.updateNextGeneration(next);

            int[] expected = { 1, 0, 1, 0, 0, 0, 1, 0, 1 };
            assertThat(next, is(expected));
        }
    }

    public static class NextGenerationTest {

        private Cells nextCells;

        @Before
        public void setUp() throws Exception {
            int[] current = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
            int width = 3;
            Model model = new Model(current, width);
            model.addNewGenerationEventListener(new NewGenerationEventListener() {

                @Override
                public void handle(Cells newCells) {
                    nextCells = newCells;
                }
            });
            model.nextGeneration();
        }

        @Test
        public void すべて生存しているとき次世代のx0y0は生存している() throws Exception {
            assertThat(nextCells.isAlive(0, 0), is(true));
        }

        @Test
        public void すべて生存しているとき次世代のx1y0は死滅している() throws Exception {
            assertThat(nextCells.isAlive(1, 0), is(false));
        }

        @Test
        public void すべて生存しているとき次世代のx2y0は生存している() throws Exception {
            assertThat(nextCells.isAlive(2, 0), is(true));
        }

        @Test
        public void すべて生存しているとき次世代のx0y1は死滅している() throws Exception {
            assertThat(nextCells.isAlive(0, 1), is(false));
        }

        @Test
        public void すべて生存しているとき次世代のx1y1は死滅している() throws Exception {
            assertThat(nextCells.isAlive(1, 1), is(false));
        }

        @Test
        public void すべて生存しているとき次世代のx2y1は死滅している() throws Exception {
            assertThat(nextCells.isAlive(2, 1), is(false));
        }

        @Test
        public void すべて生存しているとき次世代のx0y2は生存している() throws Exception {
            assertThat(nextCells.isAlive(0, 2), is(true));
        }

        @Test
        public void すべて生存しているとき次世代のx1y2は死滅している() throws Exception {
            assertThat(nextCells.isAlive(1, 2), is(false));
        }

        @Test
        public void すべて生存しているとき次世代のx2y2は生存している() throws Exception {
            assertThat(nextCells.isAlive(2, 2), is(true));
        }
    }

    public static class NextCellTest {

        @Test
        public void 誕生() throws Exception {
            int[] current = { 1, 1, 0, 1, 0, 0, 0, 0, 0 };
            int width = 3;
            Model model = new Model(current, width);
            int x = 1;
            int y = 1;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(1));
        }

        @Test
        public void 生存() throws Exception {
            int[] current = { 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0 };
            int width = 4;
            Model model = new Model(current, width);
            int x = 1;
            int y = 1;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(1));
        }

        @Test
        public void 過疎() throws Exception {
            int[] current = { 0, 0, 0, 0, 1, 1, 0, 0, 0 };
            int width = 3;
            Model model = new Model(current, width);
            int x = 1;
            int y = 1;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(0));
        }

        @Test
        public void 過密() throws Exception {
            int[] current = { 1, 1, 1, 1, 1, 0, 0, 0, 0 };
            int width = 3;
            Model model = new Model(current, width);
            int x = 1;
            int y = 1;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(0));
        }
    }

    public static class _3x3Test {

        private Model model;

        @Before
        public void setUp() throws Exception {
            int[] current = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
            int width = 3;
            model = new Model(current, width);
        }

        @Test
        public void 左上は生存する() throws Exception {
            int x = 0;
            int y = 0;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(1));
        }

        @Test
        public void 上の中央は死滅する() throws Exception {
            int x = 1;
            int y = 0;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(0));
        }

        @Test
        public void 右上は生存する() throws Exception {
            int x = 2;
            int y = 0;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(1));
        }

        @Test
        public void 中央左は死滅する() throws Exception {
            int x = 0;
            int y = 1;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(0));
        }

        @Test
        public void 中心は死滅する() throws Exception {
            int x = 1;
            int y = 1;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(0));
        }

        @Test
        public void 中央右は死滅する() throws Exception {
            int x = 2;
            int y = 1;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(0));
        }

        @Test
        public void 左下は生存する() throws Exception {
            int x = 0;
            int y = 2;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(1));
        }

        @Test
        public void 下の中央は死滅する() throws Exception {
            int x = 1;
            int y = 2;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(0));
        }

        @Test
        public void 右下は生存する() throws Exception {
            int x = 2;
            int y = 2;
            int actual = model.nextCell(x, y);
            assertThat(actual, is(1));
        }
    }

    public static class ToIndexTest {

        public static class Tester {

            private final int x;

            private final int y;

            public Tester(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public void thenIndex(int index) {
                int[] current = new int[9];
                int width = 3;
                Model model = new Model(current, width);
                int actual = model.toIndex(x, y);
                assertThat(actual, is(index));
            }
        }

        private Tester whenXY(int x, int y) {
            return new Tester(x, y);
        }

        @Test
        public void x0y0はindex0() throws Exception {
            whenXY(0, 0).thenIndex(0);
        }

        @Test
        public void x1y0はindex1() throws Exception {
            whenXY(1, 0).thenIndex(1);
        }

        @Test
        public void x2y0はindex2() throws Exception {
            whenXY(2, 0).thenIndex(2);
        }

        @Test
        public void x0y1はindex3() throws Exception {
            whenXY(0, 1).thenIndex(3);
        }

        @Test
        public void x1y1はindex4() throws Exception {
            whenXY(1, 1).thenIndex(4);
        }

        @Test
        public void x2y1はindex5() throws Exception {
            whenXY(2, 1).thenIndex(5);
        }

        @Test
        public void x0y2はindex6() throws Exception {
            whenXY(0, 2).thenIndex(6);
        }

        @Test
        public void x1y2はindex7() throws Exception {
            whenXY(1, 2).thenIndex(7);
        }

        @Test
        public void x2y2はindex8() throws Exception {
            whenXY(2, 2).thenIndex(8);
        }
    }

    public static class ToXYTest {

        public static class Tester {

            private final int index;

            public Tester(int index) {
                this.index = index;
            }

            public void thenXY(int x, int y) {
                int[] current = new int[9];
                int width = 3;
                Model model = new Model(current, width);
                int[] xy = new int[2];
                model.toXY(index, xy);
                int[] expected = { x, y };
                assertThat(xy, is(expected));
            }
        }

        private Tester whenIndex(int index) {
            return new Tester(index);
        }

        @Test
        public void index0はx0y0() throws Exception {
            whenIndex(0).thenXY(0, 0);
        }

        @Test
        public void index1はx1y0() throws Exception {
            whenIndex(1).thenXY(1, 0);
        }

        @Test
        public void index2はx2y0() throws Exception {
            whenIndex(2).thenXY(2, 0);
        }

        @Test
        public void index3はx0y1() throws Exception {
            whenIndex(3).thenXY(0, 1);
        }

        @Test
        public void index4はx1y1() throws Exception {
            whenIndex(4).thenXY(1, 1);
        }

        @Test
        public void index5はx2y1() throws Exception {
            whenIndex(5).thenXY(2, 1);
        }

        @Test
        public void index6はx0y2() throws Exception {
            whenIndex(6).thenXY(0, 2);
        }

        @Test
        public void index7はx1y2() throws Exception {
            whenIndex(7).thenXY(1, 2);
        }

        @Test
        public void index8はx2y2() throws Exception {
            whenIndex(8).thenXY(2, 2);
        }
    }
}
