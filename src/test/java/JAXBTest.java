import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.*;

public class JAXBTest {
    JAXB jaxb;

    @Before
    public void setUp(){
        jaxb = new JAXB();
    }


    @Test
    public void readBusStops() throws JAXBException, SAXException {
        jaxb.unmarshal();
        jaxb.busStopFinder(jaxb.nodes);
        jaxb.wayFinder(jaxb.ways);
        jaxb.printBusStops();
        jaxb.printResult();
    }

}