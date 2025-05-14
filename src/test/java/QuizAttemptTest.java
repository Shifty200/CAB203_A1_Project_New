import com.example.quizapp.model.Quiz;
import com.example.quizapp.model.QuizAttempt;
import com.example.quizapp.model.QuizQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class QuizAttemptTest {

    private Quiz quiz;
    private QuizAttempt attempt;

    @BeforeEach
    public void setUp() {
        ArrayList<String> answers = new ArrayList<>(Arrays.asList("A", "B", "C"));
        QuizQuestion q1 = new QuizQuestion("Q1?", answers, 0);
        QuizQuestion q2 = new QuizQuestion("Q2?", answers, 2);

        quiz = new Quiz("Sample Quiz", "General", "easy");
        quiz.addQuestion(q1);
        quiz.addQuestion(q2);

        attempt = new QuizAttempt(quiz);
    }

    @Test
    public void testSelectedAnswerCanBeSetAndRetrieved() {
        attempt.setSelectedAnswer(0, 1);
        assertEquals(1, attempt.getSelectedAnswer(0));
    }

    @Test
    public void testAnswerIsCorrect() {
        attempt.setSelectedAnswer(0, 0); // correct
        attempt.setSelectedAnswer(1, 2); // correct
        assertTrue(attempt.answerIsCorrect(0));
        assertTrue(attempt.answerIsCorrect(1));
    }

    @Test
    public void testAnswerIsIncorrect() {
        attempt.setSelectedAnswer(0, 2); // incorrect
        assertFalse(attempt.answerIsCorrect(0));
    }

    @Test
    public void testScoreCalculation() {
        attempt.setSelectedAnswer(0, 0); // correct
        attempt.setSelectedAnswer(1, 2); // correct
        assertEquals(2, attempt.getScore());
    }

    @Test
    public void testScorePercentageIsCorrect() {
        attempt.setSelectedAnswer(0, 0); // correct
        attempt.setSelectedAnswer(1, 1); // incorrect
        assertEquals(50.0, attempt.getScorePercentage());
    }

    @Test
    public void testAnsweredQuestionsCountUpdates() {
        attempt.setSelectedAnswer(0, 1);
        assertEquals(1, attempt.answeredQuestionsCount());
    }

    @Test
    public void getScorePercentageReturnsZeroWhenNoAnswersSelected() {
        // No answers selected
        assertEquals(0.0, attempt.getScorePercentage());
    }
}
