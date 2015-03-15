package exam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static java.util.stream.Collectors.*;

/**
 *
 * @author steven
 */
public class HymnPanel extends VBox {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    private Spinner<String> p2;

    private ComboBox<String> p1;


    private TextArea textArea;

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
        final HymnLoader hymnLoader = new HymnLoader();
        hymnLoader.init();
        p1 = new ComboBox<String>(){
            {
                getItems().addAll(hymnLoader.getHymns());
                setPromptText("詩");
            }
        };
        p1.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            final ObservableList<String> spinList = FXCollections.observableArrayList(IntStream.rangeClosed(1, hymnLoader.getCount(newValue)).mapToObj(Objects::toString).collect(toList()));
            p2.setValueFactory(new ListSpinnerValueFactory<>(spinList));
        });
        p2 = new Spinner<>();
        p2.valueProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            title.setText(String.format("詩 %s 首", p1.getValue()));
            if (Objects.nonNull(p2.getValue()) && !p2.getValue().isEmpty() && !Objects.isNull(p1.getValue()) && !p1.getValue().isEmpty()) {
                loadHymn();
            }
        });
        final HBox controlBar = new HBox(p1, p2);
        controlBar.setPrefHeight(USE_COMPUTED_SIZE);
        this.getChildren().addAll(title, textArea, controlBar);
    }

    private void loadHymn() {
        final String url = String.format("/hymn/H%s-%s.txt", p1.getValue(), p2.getValue());
        try {
            final String hymn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(url), StandardCharsets.UTF_8)).lines().collect(joining());
            textArea.setText(hymn);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }
}
