import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main{
    //get questions of exam from file
    private static final ArrayList<Question> questions = XMLParser.readInput();

    public static void main(String[] args) {
        try {
            if(questions != null) {
                questions.sort(Comparator.comparing(Question::getDifficulty));
                String prePrint = "==============Exam==============\n";
                prePrint += print(questions);
                prePrint += "==========Exam answers==========\n";
                prePrint += printWithAnswers(questions);
                output(prePrint);
            }
            else{
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            output("Wrong formatted input file");
        }
    }

    public static void output(String text) {
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Exception: Couldn't output!");
        }
    }

    public static String print(List<Question> questions) {
        StringBuilder printStr = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            printStr.append(i + 1).append(") ").append(questions.get(i).getQuestionText()).append("\n");
            switch (questions.get(i).getType()) {
                case "truefalse":
                    printStr.append("Answer: true false (circle the right answer)" + "\n");
                    break;
                case "multichoice":
                    for (int j = 0; j < questions.get(i).getAnswer().size(); j++) {
                        printStr.append("\t").append(j + 1).append(") ").append(questions.get(i).getAnswer().get(j)).append("\n");
                    }
                    break;
                case "short":
                    printStr.append("Answer: ____________________\n");
                    break;
                case "essay":
                    printStr.append("\n\n\n\n\n");
                    break;
            }
            printStr.append("\n");
        }
        return printStr.toString();
    }

    public static String printWithAnswers(List<Question> questions) {
        StringBuilder printStr = new StringBuilder();
        for (int i = 0; i < questions.size() - 1; i++) {
            printStr.append(i + 1).append(") ").append(questions.get(i).getQuestionText()).append("\n");
            switch (questions.get(i).getType()) {
                case "truefalse":
                    printStr.append("Answer: ").append(questions.get(i).getAnswer().get(0)).append("\n");
                    break;
                case "multichoice":
                    if (questions.get(i).getSingle()) {
                        for (int j = 0; j < questions.get(i).getAnswer().size(); j++) {
                            if ((j + 1) != questions.get(i).getSolution().get(0)) {
                                printStr.append("\t").append(j + 1).append(") ").append(questions.get(i).getAnswer().get(j)).append("\n");
                            } else {
                                printStr.append(" -> ").append(j + 1).append(") ").append(questions.get(i).getAnswer().get(j)).append("\n");
                            }
                        }
                    } else {
                        for (int j = 0; j < questions.get(i).getAnswer().size(); j++) {
                            if ((j + 1) != questions.get(i).getSolution().get(0)) {
                                printStr.append("\t").append(j + 1).append(") ").append(questions.get(i).getAnswer().get(j)).append("\n");
                            } else {
                                printStr.append(" -> ").append(j + 1).append(") ").append(questions.get(i).getAnswer().get(j)).append("\n");
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
                    printStr.append("Accepted answers: ").append(questions.get(i).getAnswer().toString()).append("\n");
                    break;
                case "essay":
                    printStr.append(questions.get(i).getAnswer().get(0)).append("\n");
                    break;
            }
            printStr.append("\n");
        }
        printStr.append(questions.size()).append(") ").append(questions.get(questions.size() - 1).getQuestionText()).append("\n");
        switch (questions.get(questions.size() - 1).getType()) {
            case "truefalse":
                printStr.append("Answer: ").append(questions.get(questions.size() - 1).getAnswer().get(0)).append("\n");
                break;
            case "multichoice":
                for (int j = 0; j < questions.get(questions.size() - 1).getAnswer().size(); j++) {
                    printStr.append("\t").append(j + 1).append(") ").append(questions.get(questions.size() - 1).getAnswer().get(j)).append("\n");
                }
                break;
            case "short":
                printStr.append("Accepted answers: ").append(questions.get(questions.size() - 1).getAnswer().toString()).append("\n");
                break;
            case "essay":
                printStr.append(questions.get(questions.size() - 1).getAnswer().get(0)).append("\n");
                break;
        }
        printStr.append("Note: To be checked manually");

        return printStr.toString();
    }
}