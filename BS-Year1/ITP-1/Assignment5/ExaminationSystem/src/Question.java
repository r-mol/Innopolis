import java.util.ArrayList;

class Question {
    //contain struct of question
    private final String type;
    private final int difficulty;
    private final String questionText;
    private boolean single;
    private final ArrayList<String> answer;
    private ArrayList<Integer> solution;


    public Question(String type, int difficulty, String questionText, ArrayList<String> answer) {
        this.type = type;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.answer = answer;
    }

    public Question(String type, int difficulty, String questionText, boolean single, ArrayList<String> answer, ArrayList<Integer> solution) {
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