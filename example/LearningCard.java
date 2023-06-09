import java.util.List;
import java.util.ArrayList;

// ------------------------------------------------------ //
public abstract class LearningCard {

    /** return the title of the card */
    public abstract String getTitel();

    /** return the front site of the card */
    public abstract List<String> getFrontContent();

    /** return the back site of the card */
    public abstract List<String> getBackContent();

    /** return combined content of front and back */
    public abstract List<String> getContent();

    /**
     * write the content to the console in a meaningful way
     */
    public abstract void printToConsole();
}

// ------------------------------------------------------ //
class SimpleCard extends LearningCard {
    private String titel;
    private List<String> back;

    public SimpleCard(String titel, List<String> back) {
        this.titel = titel;
        this.back = back;
    }

    @Override
    public List<String> getFrontContent() {
        List<String> front = new ArrayList<String>();
        front.add(" "+titel);
        return front;
    }

    @Override
    public List<String> getBackContent() {
        return back;
    }

    @Override
    public List<String> getContent() {
        List<String> content = new ArrayList<String>();
        content.add("Vorderseite: ");
        content.addAll(getFrontContent());
        content.add("Rückseite: ");
        content.addAll(getBackContent());
        return content;
    }

    @Override
    public void printToConsole() {
        for (String line : getContent()) {
            System.out.println(line);
        }
    }

    @Override
    public String getTitel() {
        return titel;
    }

}

// ------------------------------------------------------ //
class QuestionCard extends LearningCard {
    private String titel;
    private List<String> front;
    private List<String> back;

    public QuestionCard(String titel, List<String> front, List<String> back) {
        this.titel = titel;
        this.front = front;
        this.back = back;
    }

    @Override
    public List<String> getFrontContent() {
        List<String> front = new ArrayList<String>();
        front.addAll(this.front);
        return front;
    }

    @Override
    public List<String> getBackContent() {
        return back;
    }

    @Override
    public List<String> getContent() {
        List<String> content = new ArrayList<String>();
        content.add("Vorderseite: ");
        content.addAll(getFrontContent());
        content.add("Rückseite: ");
        content.addAll(getBackContent());
        return content;
    }

    @Override
    public void printToConsole() {
        for (String line : getContent()) {
            System.out.println(line);
        }
    }
    @Override
    public String getTitel() {
        return titel;
    }
}

// ------------------------------------------------------ //
// -------------- ToDo: add your own card --------------- //
// ------------------------------------------------------ //

/**
 * SingleChoiceCard
 */
class SingleChoiceCard extends LearningCard {

    private String titel;
    private List<String> front;
    private List<String> back;
    private List<String> choices;
    private List<Integer> numbers;
    private int correctAnswer;

    public SingleChoiceCard(String titel, List<String> front, List<String> back, List<String> choices,List<Integer> nums, int correctAnswer) {
        this.titel = titel;
        this.front = front;
        this.back = back;
        this.choices = choices;
        this.numbers = nums;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public List<String> getFrontContent() {
        List<String> front = new ArrayList<String>();
        front.addAll(this.front);
        for (int i = 0; i < choices.size(); i++) {
            front.add(numbers.get(i) + ". " + choices.get(i));
        }
        return front;
    }

    @Override
    public List<String> getBackContent() {
        List<String> back = new ArrayList<String>();
        back.add("Richtige Antwort: " + choices.get(correctAnswer));
        back.addAll(this.back);
        return back;
    }

    @Override
    public List<String> getContent() {
        List<String> content = new ArrayList<String>();
        content.add("Vorderseite: ");
        content.addAll(getFrontContent());
        content.add("");
        content.add("Rückseite: ");
        content.addAll(getBackContent());
        return content;
    }

    @Override
    public void printToConsole() {
        for (String line : getContent()) {
            System.out.println(line);
        }
    }
    @Override
    public String getTitel() {
        return titel;
    }
}