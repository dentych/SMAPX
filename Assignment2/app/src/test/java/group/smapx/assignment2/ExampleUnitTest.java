package group.smapx.assignment2;

import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        Timestamp now = new Timestamp(new Date().getTime());
        System.out.println("NOW: " + now.getTime());
        System.out.println("NOW + 10: " + now.getTime() +10);

        assertEquals(4, 2 + 2);
    }
}