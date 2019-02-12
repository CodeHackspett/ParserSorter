import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MenuParser {
    public static void main(String[] args) throws Exception {
        
        String orderType = args[1];
        boolean ascending;
              
        if (orderType.equals("asc")) {
            ascending = true;
        } else if (orderType.equals("desc")) {
            ascending = false;
        } else {
            throw new Exception("Invalid ordering order, please choose either 'asc' or 'desc'");
        }
        getMenu(ascending);
    }

    private static void getMenu(boolean ascending) throws ParserConfigurationException, SAXException, IOException {
        List<MenuItem> menu = parseMenu("menu.xml");
        menu.sort(Comparator.comparing(MenuItem::getName));
        if (!ascending) {
            Collections.reverse(menu);
        }
        printMenu(menu);
    }

    private static List<MenuItem> parseMenu(String fileName) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        List<MenuItem> menu = new ArrayList<>();

        NodeList nList = doc.getElementsByTagName("food");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String name = eElement
                        .getElementsByTagName("name")
                        .item(0)
                        .getTextContent();

                String price = eElement
                        .getElementsByTagName("price")
                        .item(0)
                        .getTextContent();

                String description = eElement
                        .getElementsByTagName("description")
                        .item(0)
                        .getTextContent();

                Double calories = Double.valueOf(eElement
                        .getElementsByTagName("calories")
                        .item(0)
                        .getTextContent());

                menu.add(new MenuItem(name, price, description, calories));
            }
        }

        return menu;
    }

    private static void printMenu(List<MenuItem> menu) {
        for (MenuItem item : menu) {
            System.out.println("Name: " + item.getName());
            System.out.println("Price: " + item.getPrice());
            System.out.println("Description: " + item.getDescription());
            System.out.println("Calories: " + item.getCalories());
            System.out.println();
        }
    }

    static class MenuItem {
        String name;
        String price;
        String description;
        Double calories;

        MenuItem(String name, String price, String description, Double calories) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.calories = calories;
        }

        String getName() {
            return name;
        }

        String getPrice() {
            return price;
        }

        String getDescription() {
            return description;
        }

        Double getCalories() {
            return calories;
        }
    }
}