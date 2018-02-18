import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JDOM {
    Document doc = new Document();
    List<Element> elements = new ArrayList<Element>();
    public Element readerXML() {
        try {
            SAXBuilder parser = new SAXBuilder();
            FileReader fr = new FileReader("clouds.svg");
            doc = parser.build(fr);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Element element = doc.getRootElement();
        return element;
    }

    public void listChildren(Element element) throws DataConversionException {
        //System.out.println(element.getName());
        List <Element> children = element.getChildren();
        ListIterator<Element> iter = children.listIterator();
        //for (Element e:children) {
        while(iter.hasNext()) {
            Element e=iter.next();
            if(e.getName().equals("rect")){
            printElement(e);
            changeColor(e);
            }
            if(e.getName().equals( "path")){
                changeColor(e);
            }
            if(e.getName().equals( "circle")){
                printElement(e);
                iter.add(markCenter(e));
                continue;
            }
            if(e.getContentSize()>0) {
                listChildren(e);
            }
        }
    }

    public void printElement(Element e){
        System.out.println("--------------------------------");
        System.out.println(e.getName());
        for(Attribute a: e.getAttributes()){
            System.out.println(a.getName()+" = "+a.getValue());
        }
    }

    public void changeColor(Element e) {
        if (e.getName().equals("rect")) {
            e.setAttribute("style", "fill: yellow");
        }
        if (e.getName().equals("path")) {
            e.setAttribute("style", "fill: blue");
        }
    }

    public Element markCenter(Element e) throws DataConversionException {
        String cxx = e.getAttribute("cx").getValue();
        String cyy = e.getAttribute("cy").getValue();

        Element circle = new Element("circle");
        circle.setAttribute("cx", cxx).setAttribute("cy",cyy).
                setAttribute("r","10")
                .setAttribute("fill","red").setNamespace(e.getNamespace());
        return circle;
    }

    public void OutPutSVG(){
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            FileWriter fw = new FileWriter("cloudsNew.svg");
            outputter.output(doc, fw);
            fw.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
