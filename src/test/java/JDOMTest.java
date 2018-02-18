import org.jdom2.DataConversionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMTest {
    @Test
    public void test_one(){
        JDOM first = new JDOM();
        try {
            first.listChildren(first.readerXML());
        } catch (DataConversionException e) {
            e.printStackTrace();
        }
        first.OutPutSVG();
    }
}