package elastest.io.ece.api;

import io.elastest.ece.application.EceApplication;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit test for simple App.
 */
public class EceApplicationTests
        extends TestCase {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    public void setUpStreams(){
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void cleanUpStreams(){
        System.setOut(null);
        System.setErr(null);
    }
    @Test
    public void testCheckParameters()
    {
        setUpStreams();
        String[] array = new String[1];
        array[0] = "param";
        EceApplication.checkParameters(array);
        assertEquals("...", outContent.toString());
        cleanUpStreams();
    }

    @Test
    public void testCheckHelp()
    {
        setUpStreams();
        EceApplication.checkHelp("");
        assertEquals("...", outContent.toString());
        cleanUpStreams();
    }


}