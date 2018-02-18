import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.*;

public class EqualTest {
    StAX stAX;
    JAXB jaxb;

    @Before
    public void setUp(){
        stAX = new StAX();
        jaxb = new JAXB();
    }

    @Test
    public void equal() throws JAXBException, SAXException {
        stAX.readFileStAX();
        jaxb.unmarshal();
        jaxb.busStopFinder(jaxb.nodes);
        jaxb.wayFinder(jaxb.ways);
        assertEquals(jaxb.streets, stAX.streets);
    }

}