import com.example.quizapp.model.QuizInitConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class QuizInitConfigTest {
    private static final File UPLOADEDFILE = new File("resources/testfile.txt");
    private static final double DIFFICULTY = 2;
    private static final String QUESTIONRANGE = "10–20";

    private static final File UPLOADEDFILE2 = new File ("resources/testfile2.txt");
    private static final double DIFFICULTYMIN = 1;
    private static final double DIFFICULTYMAX = 3;
    private static final String QUESTIONRANGEMIN = "1–10";
    private static final String QUESTIONRANGEMAX = "20–30";

    private QuizInitConfig quizInitConfig;

    @BeforeEach
    public void setUp() {
        quizInitConfig = new QuizInitConfig(UPLOADEDFILE, DIFFICULTY, QUESTIONRANGE);
    }

    @Test
    public void testGetUploadedFile() {
        assertEquals(UPLOADEDFILE, quizInitConfig.getUploadedFile());
    }
    @Test
    public void testGetDifficulty() {
        assertEquals(DIFFICULTY, quizInitConfig.getDifficulty());
    }
    @Test
    public void testGetQuestionRange() {
        assertEquals(QUESTIONRANGE, quizInitConfig.getQuestionRange());
    }

}
