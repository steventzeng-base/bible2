package exam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static java.util.stream.Collectors.*;

/**
 *
 * @author steven
 */
public class HymnPanel extends VBox {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public HymnPanel() {
        render();
    }

    private void render() {

        setPrefHeight(USE_COMPUTED_SIZE);
        Text title = new Text();
        title.setStyle("-fx-font:40pt monospace; -fx-font-smoothing-type: lcd; -fx-font-family:'Source Han Sans Heavy';");
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font:40pt monospace; -fx-font-smoothing-type:lcd; -fx-background-color: black;");
        textArea.setText(" ");
        VBox.setVgrow(textArea, Priority.ALWAYS);
        p1 = new TextField() {
            {
                setPromptText("詩");
            }
        };
        p1.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            p2.setText("");
        });
        p2 = new TextField() {
            {
                setPromptText("節");
            }
        };
        p2.textProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            title.setText(String.format("詩 %s 首", p1.getText()));
            if (Objects.nonNull(p2.getText()) && !p2.getText().isEmpty() && !Objects.isNull(p1.getText()) && !p1.getText().isEmpty()) {
                loadHymn();
            }
        });
        final HBox controlBar = new HBox(p1, p2);
        controlBar.setPrefHeight(USE_COMPUTED_SIZE);
        this.getChildren().addAll(title, textArea, controlBar);
    }

    private TextField p2;

    private TextField p1;

    private void loadHymn() {
        final String url = String.format("/hymn/H%s-%s.txt", p1.getText(), p2.getText());
        try {
            final String hymn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(url), StandardCharsets.UTF_8)).lines().collect(joining());
            textArea.setText(hymn);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    private TextArea textArea;
}
