import com.example.quizapp.model.QuizInitConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class QuizInitConfigTest {
    private static final String FILEPATH = "src/test/resources/testfile.txt";
    private static final File UPLOADEDFILE = new File(FILEPATH);
    private static final double DIFFICULTY = 2;
    private static final String QUESTIONRANGE = "10–20";

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

    @Test
    public void testReadLinesFromFile() throws IOException {
        assertEquals("This is a test file.\n", QuizInitConfig.readLinesFromFile(FILEPATH));
    }
}
