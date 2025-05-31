import com.example.quizapp.model.Quiz;
import com.example.quizapp.model.QuizQuestion;
import com.example.quizapp.model.QuizAppUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class QuizAppUtilTest {
    private static final int[] INTARRAY = new int[] {1, 2, 3, 4};
    private static final String JSONOBJECT = """
            {
                "questions": [
                    {
                        "question": "Question text",
                        "options": ["A", "B"],
                        "correctIndex": 0
                    }
                ]
            }
            """;

    private static final String QUIZNAME = "Quiz Name";
    private static final String TOPIC = "Topic";
    private static final String DIFFICULTY = "easy";

    @Test
    public void testFromString() {
        String intArrayAsString = Arrays.toString(INTARRAY);
        assertTrue(Arrays.equals(INTARRAY, QuizAppUtil.fromString(intArrayAsString)));
    }

    @Test
    public void testParseAIResponse() {
        Quiz quiz = new Quiz(QUIZNAME, TOPIC, DIFFICULTY);
        QuizQuestion question = new QuizQuestion("Question text",
                new ArrayList<String>(Arrays.asList("A", "B")), 0);
        quiz.addQuestion(question);
        Quiz parsedQuiz = QuizAppUtil.parseAIResponse(JSONOBJECT, QUIZNAME, TOPIC, DIFFICULTY);
        assertTrue(Objects.equals(quiz, parsedQuiz));
    }
}
