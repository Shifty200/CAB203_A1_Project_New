import com.example.quizapp.model.QuizAppAlert;

import javafx.application.Platform;
import javafx.scene.control.Alert;
// import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class QuizAppAlertTest{

    @Test
    public void testQuizAppAlert() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        //Platform.startup(() -> {}); // Start the JavaFX platform if not already started
        Platform.runLater(() -> {
            try {
                QuizAppAlert testAlert = new QuizAppAlert();
                Alert alert = testAlert.createAlert("Test Title", "Test Header", "Test Content");

                assertEquals(Alert.AlertType.WARNING, alert.getAlertType());
                assertEquals("Test Title", alert.getTitle());
                assertEquals("Test Header", alert.getHeaderText());
                assertEquals("Test Content", alert.getContentText());
                assertNotNull(alert.getGraphic());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }
}