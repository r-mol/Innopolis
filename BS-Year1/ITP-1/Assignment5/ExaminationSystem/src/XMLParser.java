import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLParser {
    private static final String FILENAME = "input.xml";
    private static final ArrayList<Question> questions = new ArrayList<>();

    public static ArrayList<Question> readInput() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        ArrayList <Question> questions = new ArrayList<>();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(FILENAME));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("question");

            for (int index = 0; index < list.getLength(); index++) {

                Node node = list.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type = element.getAttribute("type");

                    int difficulty = Integer.parseInt(element.getElementsByTagName("difficulty").item(0).getTextContent());
                    String questionText = element.getElementsByTagName("questiontext").item(0).getTextContent();
                    boolean single;
                    ArrayList<String> answer = new ArrayList<>();
                    String solution;


                    switch (type) {
                        //case for multichoice tasks
                        case "multichoice":
                            single = Boolean.parseBoolean(element.getElementsByTagName("single").item(0).getTextContent());
                            for (int i = 0; i < 4; i++) {
                                answer.add(element.getElementsByTagName("answer").item(i).getTextContent());
                            }
                            ArrayList<Integer> sol = new ArrayList<>();
                            solution = element.getElementsByTagName("solution").item(0).getTextContent();
                            for (Character c : solution.toCharArray()) {
                                if (!c.equals(',') && !c.equals(' ')) {
                                    sol.add(Character.getNumericValue(c));
                                }
                            }
                            questions.add(new Question(type, difficulty, questionText, single, answer, sol));

                            //case for short tasks
                            break;
                        case "short":
                            answer.add(element.getElementsByTagName("answers").item(0).getTextContent());
                            questions.add(new Question(type, difficulty, questionText, answer));

                            //case for true and essay tasks
                            break;
                        case "truefalse":
                        case "essay":
                            answer.add(element.getElementsByTagName("answer").item(0).getTextContent());
                            questions.add(new Question(type, difficulty, questionText, answer));
                            break;

                        //exception
                        default:
                            throw new NullPointerException();
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            return null;
        }
        return questions;
    }
}