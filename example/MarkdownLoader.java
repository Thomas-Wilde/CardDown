/* ToDo: import standard java libraries you need e.g. java.io, java.utils, ... */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MarkdownLoader {
    // ---------------------------------------------------- //
    /**
     * Load the learninge cards from the given file.
     */
    public static List<LearningCard> loadCardFile(String file_path) {
        List<LearningCard> cards = new ArrayList<LearningCard>();

        // step 1: read the file
        List<String> lines = null;
        Path actual_file_path = Paths.get(file_path);
        try {
            lines = Files.readAllLines(actual_file_path);
        } catch (Exception e) {
            System.out.println("[FILE ERROR]: " + e.getMessage());
        }

        // step 2: parse the lines
        for (int i = 0; i < lines.size(); i++) {
            int jump = 0;
            switch (identifyCardType(lines.get(i))) {
                case 0:
                    jump = loadSimpleCard(lines, cards, i);
                    break;
                case 1:
                    jump = loadQuestionCard(lines, cards, i);
                    break;
                case 2:
                    jump = loadSingleChoiceCard(lines, cards, i);
                    break;
                default:
                    System.out.println("[ERROR]: Unknown card type. Skipping line.");
                    continue;
            }
            i = jump-1;
        }
        return cards;
    }


    // ---------------------------------------------------- //
    public static int loadSimpleCard(List<String> lines, List<LearningCard> c, int index) {
        String title = lines.get(index).substring(2);
        List<String> back = new ArrayList<String>();

        int i = index+1;

        for (; i < lines.size() && identifyCardType(lines.get(i))==-1; i++) {
            back.add(lines.get(i));
        }

        c.add(new SimpleCard(title, back));
        return i;
    }

    // ---------------------------------------------------- //
    public static int loadQuestionCard(List<String> lines, List<LearningCard> c, int index) {
        String title = lines.get(index).substring(2, lines.get(index).length()-10);
        List<String> front = new ArrayList<String>();
        List<String> back = new ArrayList<String>();

        int i = index+1;

        int mode = -1; // -1 = no content, 0 = front, 1 = back

        for (; i < lines.size() && identifyCardType(lines.get(i))==-1; i++) {
            if(lines.get(i).startsWith("## ")  && lines.get(i).endsWith("{BACK}")) {
                mode =  1;
                continue;
            }

            if(lines.get(i).startsWith("## ")  && lines.get(i).endsWith("{FRONT}")) {
                mode =  0;
                front.add(lines.get(i).substring(3, lines.get(i).length()-7));
                continue;
            }

            if(mode == -1) {
                continue;
            }

            if(mode == 0) {
                front.add(lines.get(i));
            } else {
                back.add(lines.get(i));
            }
        }

        c.add(new QuestionCard(title, front, back));
        return i;
    }

    // ---------------------------------------------------- //
    public static int loadSingleChoiceCard(List<String> lines, List<LearningCard> c, int index) {
        String title = lines.get(index).substring(2, lines.get(index).length()-4);
        List<String> front = new ArrayList<String>();
        List<String> back = new ArrayList<String>();
        List<String> choices = new ArrayList<String>();
        List<Integer> numbers = new ArrayList<Integer>();
        int correctAnswer = -1;

        int i = index+1;

        int mode = 0; // 0 = front, 1 = no content, 2 = back

        for (; i < lines.size() && identifyCardType(lines.get(i))==-1; i++) {
            if(lines.get(i).startsWith("## Solution") && mode!=2  ) {
                mode = 2;
                continue;
            }

            if(mode == 0) {
                // check if line is a choice
                if(lines.get(i).matches("^(\\d)+\\.\\h(.)*")){ // Start der Zeile, beliebig viele Ziffern, Punkt, beliebig viele Leerzeichen, beliebig viele Zeichen
                    Matcher m = Pattern.compile("^((\\d+)\\.\\h)((.)*)").matcher(lines.get(i));
                    m.find();
                    numbers.add(Integer.parseInt(m.group(2)));
                    choices.add(m.group(3));
                    continue;
                }

                // check if line is the correct answer
                if(lines.get(i).startsWith("->")) {
                    correctAnswer = Integer.parseInt(lines.get(i).substring(3));
                    correctAnswer = numbers.indexOf(correctAnswer);
                    if(correctAnswer == -1) {
                        System.err.println("[CARD_ERROR]: Correct answer is not a valid choice.");
                        return i;
                    }

                    mode = 1;
                    continue;
                }

                //if not, add to front
                front.add(lines.get(i));
                continue;
            }
            if(mode == 2) {
                back.add(lines.get(i));
            }
        }

        if(correctAnswer == -1) {
            System.err.println("[CARD_ERROR]: Correct answer is not a valid choice. Something went wrong.");
            return i;
        }

        c.add(new SingleChoiceCard(title, front, back, choices, numbers, correctAnswer));
        return i;
    }

    // ---------------------------------------------------- //
    public static int identifyCardType(String line) {
        if(line.startsWith("# ")) {
            if(line.endsWith("{QUESTION}")) {
               return 1;
            }
            if(line.endsWith("{SC}")){
                return 2;
            }
            return 0;
        }
        return -1; // no card found
    }

    //Steps to add a new card type:
    // 1. Add a new method loadNewCardType
    // 2. Add the new card type to identifyCardType
    // 3. Add the new card type to the switch statement in loadCardFile
    // Done!

    // ---------------------------------------------------- //
    public static void main(String[] args) {
        HTMLCardGenerator generator = new HTMLCardGenerator();
        List<LearningCard> cards = loadCardFile("cards.md");
        for (LearningCard card : cards) {
            card.printToConsole();
        }

        generator.createHTMLCards(cards, "cards.html");
    }
}