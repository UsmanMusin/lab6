import jdk.internal.org.xml.sax.SAXException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StAX{
    String fileName = "UfaCenter.xml";
    String schemaName = "osm.xsd";
    Map<String, String> tags = new TreeMap<>();
    Map<String, Street> streets = new TreeMap<>();
    List<String> busStops = new ArrayList<>();
    Street street;
    String currentClassStreet;
    boolean flagBusStop = false;
    boolean flagWay = false;
    boolean nodeOpen = false;
    private String nameStreet = "";


    public boolean validateXML() throws org.xml.sax.SAXException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            File schemaLocation = new File(schemaName);
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void readFileStAX() throws org.xml.sax.SAXException {
        try {
            if (validateXML()) {
                XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().
                        createXMLStreamReader(new FileInputStream(fileName));
                while (xmlStreamReader.hasNext()) {
                    xmlStreamReader.next();
                    if (xmlStreamReader.isStartElement() || xmlStreamReader.isEndElement()) {
                        busStopFinder(xmlStreamReader);
                        wayFinder(xmlStreamReader);
                        allTags(xmlStreamReader);
                    }
                }
            } else {
                System.out.println("Невалидный xml документ");
            }


        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void allTags(XMLStreamReader xsr){
        if(xsr.getLocalName().equals("tag") && xsr.isStartElement()){
            tags.put(xsr.getAttributeValue("","k"),
                    xsr.getAttributeValue("","v"));
        }
    }

    public void allTagsPrint(){
        for(Map.Entry e : tags.entrySet()){
            System.out.println(e.getKey()+" "+ e.getValue());

        }
    }



    public void busStopFinder(XMLStreamReader xsr) throws XMLStreamException {
        if (xsr.getLocalName().equals("node") && xsr.isStartElement()) {
            nodeOpen = true;
            flagBusStop=false;
        }
        if (xsr.getLocalName().equals("node") && xsr.isEndElement()) {
            flagBusStop = false;
            nodeOpen = false;
        }
        if (nodeOpen && xsr.getLocalName().equals("tag") && xsr.isStartElement()) {
            if (xsr.getAttributeValue("", "k").equals("highway") &&
                    xsr.getAttributeValue("", "v").equals("bus_stop")) {
                flagBusStop = true;
            }
            if (flagBusStop && xsr.getAttributeValue("", "k").equals("name")) {
                busStops.add(xsr.getAttributeValue("","v"));
            }
        }
    }


    public void wayFinder(XMLStreamReader xsr) throws XMLStreamException {
        if (xsr.getLocalName().equals("way") && xsr.isStartElement()) {
            flagWay = true;

            currentClassStreet = null;
            nameStreet = null;
        }
        if (xsr.getLocalName().equals("way") && xsr.isEndElement()) {
            flagWay = false;

        }

        if (xsr.getLocalName().equals("way") && xsr.isEndElement() &&
                currentClassStreet != null && nameStreet != null) {
            if (streets.containsKey(nameStreet)) {
                streets.get(nameStreet).addSegment(currentClassStreet);
            } else {
                street = new Street(nameStreet);
                street.addSegment(currentClassStreet);
                streets.put(nameStreet, street);
            }
        }

        if (xsr.getLocalName().equals("tag") && xsr.isStartElement() && flagWay) {
            if (xsr.getAttributeValue("", "k").equals("highway")) {
                currentClassStreet = xsr.getAttributeValue("", "v");
            }
            if (xsr.getAttributeValue("", "k").equals("name")) {
                nameStreet = xsr.getAttributeValue("", "v");
            }
        }


    }

    public void printResult(StAX stAX){
        for (Street sd : stAX.streets.values()){
            System.out.println("Название улицы: " +  sd.getName());
            sd.printClasses();
            System.out.println("---------------------------------------------");
        }
        System.out.println(stAX.busStops);
    }

}