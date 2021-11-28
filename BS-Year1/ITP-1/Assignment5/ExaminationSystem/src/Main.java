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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    //get questions of exam from file
    private static final ArrayList<Exam> questions = XmlParser.parse();

    public static void main(String[] args) {

        try {
            //sort the ArrayList of questions
            questions.sort(Comparator.comparing(Exam::getDifficulty));
            FileWriter writer = new FileWriter("output.txt");
            if (questions.size() > 0) {
                //print exam without answers
                printExam(questions, writer);
                //print exam with answers
                printExamAnswers(questions, writer);
            } else {
                writer.write("Wrong formatted input file");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Exception: Couldn't output!");
        }
    }

    public static void printExam(ArrayList<Exam> questions, FileWriter writer) throws IOException {
        writer.write("==============Exam==============\n");
        for (int i = 0; i < questions.size(); i++) {
            writer.write((i + 1) + ") " + questions.get(i).getQuestionText() + "\n");
            switch (questions.get(i).getType()) {
                case "truefalse":
                    writer.write("Answer: true false (circle the right answer)" + "\n");
                    break;
                case "multichoice":
                    for (int j = 0; j < questions.get(i).getAnswer().size(); j++) {
                        writer.write("\t" + (j + 1) + ") " + questions.get(i).getAnswer().get(j) + "\n");
                    }
                    break;
                case "short":
                    writer.write("Answer: ____________________\n");
                    break;
                case "essay":
                    writer.write("\n\n\n\n\n");
                    break;
            }
            writer.write("\n");
        }
    }

    public static void printExamAnswers(ArrayList<Exam> questions, FileWriter writer) throws IOException {
        writer.write("==========Exam answers==========\n");
        for (int i = 0; i < questions.size() - 1; i++) {
            writer.write((i + 1) + ") " + questions.get(i).getQuestionText() + "\n");
            switch (questions.get(i).getType()) {
                case "truefalse":
                    writer.write("Answer: " + questions.get(i).getAnswer().get(0) + "\n");
                    break;
                case "multichoice":
                    if (questions.get(i).getSingle()) {
                        for (int j = 0; j < questions.get(i).getAnswer().size(); j++) {
                            if ((j + 1) != questions.get(i).getSolution().get(0)) {
                                writer.write("\t" + (j + 1) + ") " + questions.get(i).getAnswer().get(j) + "\n");
                            } else {
                                writer.write(" -> " + (j + 1) + ") " + questions.get(i).getAnswer().get(j) + "\n");
                            }
                        }
                    } else {
                        for (int j = 0; j < questions.get(i).getAnswer().size(); j++) {
                            if ((j + 1) != questions.get(i).getSolution().get(0)) {
                                writer.write("\t" + (j + 1) + ") " + questions.get(i).getAnswer().get(j) + "\n");
                            } else {
                                writer.write(" -> " + (j + 1) + ") " + questions.get(i).getAnswer().get(j) + "\n");
                                questions.get(i).getSolution().remove(0);
                                if (questions.get(i).getSolution().size() == 0) {
                                    questions.get(i).getSolution().add(0);
                                }
                            }
                        }
                    }

                    break;
                case "short":
                    String temp = questions.get(i).getAnswer().get(0);
                    temp = temp.replaceAll(",", ", ");
                    questions.get(i).getAnswer().remove(0);
                    questions.get(i).getAnswer().add(temp);
                    writer.write("Accepted answers: " + questions.get(i).getAnswer().toString() + "\n");
                    break;
                case "essay":
                    writer.write(questions.get(i).getAnswer().get(0) + "\n");
                    break;
            }
            writer.write("\n");
        }
        writer.write((questions.size()) + ") " + questions.get(questions.size() - 1).getQuestionText() + "\n");
        switch (questions.get(questions.size() - 1).getType()) {
            case "truefalse":
                writer.write("Answer: " + questions.get(questions.size() - 1).getAnswer().get(0) + "\n");
                break;
            case "multichoice":
                for (int j = 0; j < questions.get(questions.size() - 1).getAnswer().size(); j++) {
                    writer.write("\t" + (j + 1) + ") " + questions.get(questions.size() - 1).getAnswer().get(j) + "\n");
                }
                break;
            case "short":
                writer.write("Accepted answers: " + questions.get(questions.size() - 1).getAnswer().toString() + "\n");
                break;
            case "essay":
                writer.write(questions.get(questions.size() - 1).getAnswer().get(0) + "\n");
                break;
        }
        writer.write("Note: To be checked manually");
    }
}

class XmlParser {

    private static final String FILENAME = "input.xml";
    private static final ArrayList<Exam> questions = new ArrayList<>();

    public static ArrayList<Exam> parse() {

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(FILENAME));

            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            // get <question>
            NodeList list = doc.getElementsByTagName("question");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    // get question's attribute
                    String type = element.getAttribute("type");

                    // get text
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
                            questions.add(new Exam(type, difficulty, questionText, single, answer, sol));

                            //case for short tasks
                            break;
                        case "short":
                            answer.add(element.getElementsByTagName("answers").item(0).getTextContent());
                            questions.add(new Exam(type, difficulty, questionText, answer));

                            //case for true and essay tasks
                            break;
                        case "truefalse":
                        case "essay":
                            answer.add(element.getElementsByTagName("answer").item(0).getTextContent());
                            questions.add(new Exam(type, difficulty, questionText, answer));
                            break;

                        //exception
                        default:
                            throw new NullPointerException();
                    }

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Wrong formatted input file");
        }
        return questions;
    }
}

class Exam {
    //contain struct of question
    private final String type;
    private final int difficulty;
    private final String questionText;
    private boolean single;
    private final ArrayList<String> answer;
    private ArrayList<Integer> solution;


    public Exam(String type, int difficulty, String questionText, ArrayList<String> answer) {
        this.type = type;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.answer = answer;
    }

    public Exam(String type, int difficulty, String questionText, boolean single, ArrayList<String> answer, ArrayList<Integer> solution) {
        this.type = type;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.answer = answer;
        this.solution = solution;
        this.single = single;
    }

    public String getType() {
        return type;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getQuestionText() {
        return questionText;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public ArrayList<Integer> getSolution() {
        return solution;
    }

    public boolean getSingle() {
        return single;
    }
}