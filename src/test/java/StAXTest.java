import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class StAXTest {
    StAX stAX;

    @Before
    public void setUp(){
        stAX = new StAX();
    }

    @Test
    public void validateXML_test() throws Exception {
        assertTrue(stAX.validateXML());
    }

    @Test
    public void readStreets() throws SAXException {
        stAX.readFileStAX();
        stAX.printResult(stAX);
        stAX.allTagsPrint();
    }

}