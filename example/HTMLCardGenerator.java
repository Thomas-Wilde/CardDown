import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLCardGenerator {
  public void createHTMLCards(List<LearningCard> cards, String output_file) {
    /* ToDo: insert your code here */
    List<String> html_cards = new ArrayList<String>();
    html_cards.add("<!DOCTYPE html><html lang='de'><style>:root {--main-color: rgb(88, 155, 255);--divider-color: rgba(255, 255, 255, 0.479);}body {height: 100%;margin-bottom: 0;margin-top: 0;background-color: rgb(35, 35, 35);font-family: sans-serif;}.all {align-items: center;text-align: left;font-size: large;background-color: rgb(48, 45, 54);border: none;outline: none;border-radius: 17px;width: auto;height: 100%;margin-left: auto;margin-right: auto;max-width: 70rem;}.LearningCard {height: 100%;}.titel {margin-top: 5px;color: rgb(255, 255, 255);text-align: center;outline: none;font-size: 36px;border-bottom: 5px Solid #fff;padding-bottom: 10px;padding-top: 10px;width: 100%;background-color: rgb(48, 45, 54);border-radius: 17px 17px 0 0;}.titlefix {background-color: rgb(35, 35, 35);position: sticky;margin-top: 0;padding-top: 5px;top: 0;}.content {color: rgb(255, 255, 255);border: none;text-align: left;outline: none;padding-left: 60px;padding-right: 60px;height: 100%;bottom: 0;font-size: 22px;}.noanswer {padding-bottom: 10px;border-radius: 17px;}.link {color: rgb(221, 1, 255);}p {overflow-wrap: break-word;line-height: 100%;}.prev,.next {cursor: pointer;position: relative;width: auto;padding: 5px;margin-top: -22px;color: white;font-weight: bold;font-size: 30px;padding: 10px 20px;border-radius: 0 6px 6px 0;user-select: none;z-index: 10;}.prevnext-wrapper {max-width: 70rem;margin-left: auto;margin-right: auto;width: 100%;position: fixed;top: 50%;display: flex;justify-content: space-between;}.reveal {background-color: rgb(40, 40, 40);font-weight: bold;font-size: 30px;border-radius: 0 0 17px 17px;z-index: 2;}.revealfix {z-index: 1;cursor: pointer;align-items: center;position: sticky;margin-left: auto;margin-right: auto;margin-bottom: auto;margin-top: auto;color: rgb(255, 255, 255);user-select: none;width: 100%;bottom: 0;text-align: center;padding-bottom: 10px;background-color: rgb(35, 35, 35);}.next {border-radius: 6px 0 0 6px;}.prev:hover,.next:hover {transition: 0.6s ease;background-color: rgba(0, 0, 0, 0.8);}.reveal:hover {transition: 0.6s ease;background-color: rgb(29, 29, 29);}.rev:active {transition: 0.2s ease;background-color: rgba(0, 0, 0);color: rgb(0, 158, 5);}.hide:active {transition: 0.2s ease;background-color: rgba(0, 0, 0);color: red;}</style><style media='print'>@page {size: A4;margin: 0;}.prev,.next,.reveal {display: none;}.card,.answer {background-color: white;color: black;}hr {color: black;}</style><head><title>LearningCard</title></head><body onbeforeprint='changePrintLayout()'><div class='all'><div class='prevnext-wrapper'><a class='prev' onclick='previousSlide()'>❮</a><a class='next' onclick='nextSlide()'>❯</a></div>".replace('\'', '"'));
    for (LearningCard card : cards) {
      html_cards.addAll(card2HTML(card));
    }
    html_cards.add("<div class=\"revealfix\">");
    html_cards.add("<div class=\"revealfix\"><a onclick=\"revealAnswer()\" class=\"reveal\">Reveal</a></div>");
    html_cards.add("</div></body>");

    html_cards.add("<script>    let slideIndex = 1;    let learnCards = document.getElementsByClassName('LearningCard');    let cards = document.getElementsByClassName('card');    let answers = document.getElementsByClassName('answer');    showSlides(slideIndex);    document.addEventListener('keydown', function (event) {        if (event.keyCode == 39) {            nextSlide();        }        if (event.keyCode == 37) {            previousSlide();        }    });    function changePrintLayout() {    }    function revealAnswer() {        let rev = document.getElementsByClassName('reveal')[0];        let ans = learnCards[slideIndex - 1].getElementsByClassName('answer')[0];        if (ans == undefined) {            console.log('No answer for card: ' + slideIndex);            return;        }        if (ans.style.display == 'none') {            ans.style.display = 'block';            rev.innerHTML = 'Hide';            rev.classList.add('hide');            rev.classList.remove('rev');            cards[slideIndex - 1].style.display = 'none';        } else {            ans.style.display = 'none';            rev.innerHTML = 'Reveal';            rev.classList.remove('hide');            rev.classList.add('rev');            cards[slideIndex - 1].style.display = 'block';        }    }    function nextSlide() {        showSlides(slideIndex += 1);    }    function previousSlide() {        showSlides(slideIndex -= 1);    }    function showSlides() {                let rev = document.getElementsByClassName('reveal')[0];        let revfix = document.getElementsByClassName('revealfix')[0];        if (slideIndex > cards.length) { slideIndex = 1 };        if (slideIndex < 1) { slideIndex = cards.length };        for (let answer of answers) {            answer.style.display = 'none';        }        for (let slide of cards) {            slide.style.display = 'none';        }        cards[slideIndex - 1].style.display = 'block';        let ans = learnCards[slideIndex - 1].getElementsByClassName('answer')[0];        if (ans == undefined) {            rev.style.display = 'none';            revfix.style.display = 'none';            learnCards[slideIndex - 1].classList.add('noanswer');        } else {            revfix.style.display = 'block';            rev.style.display = 'block';            rev.innerHTML = 'Reveal';            rev.classList.add('rev');        }    }</script>".replace('\'', '"'));
    html_cards.add("</html>");

    createFile(output_file, html_cards);
  }

  // create or overwrite file
  public void createFile(String file_path, List<String> content) {
    Path actual_file_path = Paths.get(file_path);
    try {
      Files.write(actual_file_path, content);
    } catch (Exception e) {
      System.out.println("[FILE ERROR]: " + e.getMessage());
    }
  }

  public List<String> card2HTML(LearningCard card) {
    List<String> output = new ArrayList<String>();
    output.add("<div class=\"LearningCard\">");
    output.add("<div class=\"card\">");
    output.add("<div class=\"titlefix\">");
    output.add("<h1 class=\"titel\">" + card.getTitel() + "</h1></div>");
    output.add("<div class=\"content\">");

    for (String line : card.getFrontContent()) {
      line = mdline2HTML(cleanIncompatible(line));
      output.add("<p>" + line + "</p>");
    }

    output.add("</div></div>");

    output.add("<div class=\"answer\">");
    output.add("<div class=\"titlefix\">");
    output.add("<h1 class=\"titel\">" + "Antwort</h1></div>");
    output.add("<div class=\"content\">");

    for (String line : card.getBackContent()) {
      String modLine = mdline2HTML(cleanIncompatible(line));
      output.add("<p>" + modLine + "</p>");
    }

    output.add("</div></div></div>");

    return output;
  }

  public String mdline2HTML(String line) {
    Matcher m;
    String front = "";
    String back = "";

    //heading and ignore heading 1
    if (line.startsWith("#")) {
      int heading_level = 1;
      while (line.charAt(heading_level) == '#') {
        heading_level++;
      }
      System.out.println("Heading level " + heading_level + " found in line: " + line);
      front = "<h" + heading_level + ">";
      line = line.substring(heading_level + 1);
      back = "</h" + heading_level + ">";
    } else if(line.startsWith("- ") || line.startsWith("* ") || line.startsWith("+ ")){
      System.out.println("Unordered list found: " + line);
      line = "<li>" + line.substring(2) + "</li>";
    }

    //inline-link
    m = Pattern.compile("\\[.*\\]\\(.*\\)").matcher(line);
    if (m.find()) {
      String link = m.group();
      String text = link.substring(1, link.indexOf("]("));
      String url = link.substring(link.indexOf("](") + 2, link.length() - 1);
      System.out.println("Link found: " + link + " -> " + url + " -> " + text);
      line = line.replace(link, "<a class=\"link\" href=\"" + url + "\">" + text + "</a>");
    }

    //inline code
    m = Pattern.compile("`.*`").matcher(line);
    if (m.find()) {
      String code = m.group();
      System.out.println("Code found: " + code);
      line = line.replace(code, "<code>" + code.substring(1, code.length() - 1) + "</code>");
    }

    //bold
    m = Pattern.compile("\\*\\*.*\\*\\*").matcher(line);
    if (m.find()) {
      String bold = m.group();
      System.out.println("Bold found: " + bold);
      System.out.println("Bold found: " + "<b>" + bold.substring(2, bold.length() - 2) + "</b>");
      line = line.replace(bold, "<b>" + bold.substring(2, bold.length() - 2) + "</b>");
    }

    //italic
    m = Pattern.compile("\\*.*\\*").matcher(line);
    if (m.find()) {
      String italic = m.group();
      System.out.println("Italic found: " + italic);
      line = line.replace(italic, "<i>" + italic.substring(1, italic.length() - 1) + "</i>");
    }

    //strikethrough
    m = Pattern.compile("~~.*~~").matcher(line);
    if (m.find()) {
      String strikethrough = m.group();
      System.out.println("Strikethrough found: " + strikethrough);
      line = line.replace(strikethrough, "<s>" + strikethrough.substring(2, strikethrough.length() - 2) + "</s>");
    }

    return front + line + back;
  }

  public String cleanIncompatible(String line){

    //delete all heading 1
    if (line.startsWith("# ")) {
      line = line.substring(2);
    }

    return line;
  }
}