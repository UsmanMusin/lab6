import generated.Node;
import generated.Osm;
import generated.Tag;

import generated.Way;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JAXB {
    Map<String, Street> streets = new TreeMap<>();
    List<Node> nodes = new ArrayList<>();
    List<Way> ways = new ArrayList<>();
    List<String> busStops;
    String currentClassStreet;
    String nameStreet;
    boolean flagBusStop = false;


    public void unmarshal() throws JAXBException, SAXException {
        JAXBContext jxc = JAXBContext.newInstance("generated");
        Unmarshaller u = jxc.createUnmarshaller();

        StreamSource ss = new StreamSource("UfaCenter.xml");
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = sf.newSchema(new StreamSource("osm.xsd"));
        u.setSchema(schema);

        JAXBElement<Osm> je = (JAXBElement) u.unmarshal(ss, Osm.class);

        Osm osm = je.getValue();

        for (Object o : osm.getBoundOrUserOrPreferences()) {
            if (o instanceof Node) {
                nodes.add((Node) o);
            }
            if(o instanceof  Way){
                ways.add((Way) o);
            }
        }
    }

    public void busStopFinder(List<Node> nodes){
        busStops = new ArrayList<>();
        String name;
        for (Node node: nodes) {
            flagBusStop = false;
            name=null;
            for (Tag tag: node.getTag()) {
                if (tag.getK().equals("highway") && tag.getV().equals("bus_stop")) {
                    flagBusStop = true;
                }
                if (tag.getK().equals("name") ){
                  name= tag.getV();
                }
            }
            if (flagBusStop && name!=null)
                  busStops.add(name);
        }
    }

    public void wayFinder(List<Way> ways){
        for(Way way: ways){

            boolean highwayFlag = false;
            boolean nameFlag = false;
            for(Object o: way.getRest()){
                if(o instanceof Tag) {
                    Tag wayTag = (Tag) o;

                    if (wayTag.getK().equals("highway")) {
                        currentClassStreet = wayTag.getV();
                        highwayFlag = true;
                    }
                    if (wayTag.getK().equals("name")) {
                        nameStreet = wayTag.getV();
                        nameFlag = true;
                    }
                }
            }

            if(highwayFlag && nameFlag){
                if(streets.containsKey(nameStreet)){
                    streets.get(nameStreet).addSegment(currentClassStreet);
                }
                else {

                    Street street = new Street(nameStreet);
                    street.addSegment(currentClassStreet);
                    streets.put(nameStreet, street);
                }
            }

        }
    }

    public void printBusStops(){
        for (String s: busStops){
            System.out.println(s);
        }
    }

    public void printResult(){
        for (Street sd : streets.values()){
            System.out.println("Название улицы: " +  sd.getName());
            sd.printClasses();
            System.out.println("---------------------------------------------");
        }
    }
}
