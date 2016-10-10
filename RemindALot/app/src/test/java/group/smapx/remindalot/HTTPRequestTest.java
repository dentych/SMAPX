package group.smapx.remindalot;

import org.junit.Assert;
import org.junit.Test;

import group.smapx.remindalot.Create.AddressValidator;
import group.smapx.remindalot.Create.HTTPRequestExecutor;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HTTPRequestTest {
    @Test
    public void addition_isCorrect() throws Exception {

        AddressValidator validator = new AddressValidator();
        validator.validate("Ekkodalen 4 8210");
    }
}