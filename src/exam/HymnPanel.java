package exam;

import exam.utils.UIHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
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

    private Spinner<String> p1;

    private Spinner<String> p2;

    private final ObservableList<String> hymns = FXCollections.observableArrayList();

    private final ObservableList<String> section = FXCollections.observableArrayList();

    private TextArea textArea;

    public HymnPanel() {
        render();
    }

    private void render() {
        setPrefHeight(USE_COMPUTED_SIZE);
        Text title = new Text();
        title.getStyleClass().add("hymnTitle");
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.getStyleClass().add("kanban");
        textArea.setText(" ");
        VBox.setVgrow(textArea, Priority.ALWAYS);
        p1 = new Spinner<>(UIHelper.createSpinnerFactory(hymns, Objects::toString));
        p1.setPrefWidth(USE_COMPUTED_SIZE);
        final HymnLoader hymnLoader = new HymnLoader();
        hymnLoader.init();
        hymns.addAll(hymnLoader.getHymns());
        p1.valueProperty().addListener((observable, oldValue, newValue) -> {
            section.clear();
            if (Objects.nonNull(newValue)) {
                section.addAll(IntStream.rangeClosed(1, hymnLoader.getCount(newValue)).mapToObj(Objects::toString).collect(toList()));
            }
        });
        p1.setOnScroll(event -> UIHelper.mouseWheelHandler(event, 5));
        
        p2 = new Spinner<>(UIHelper.createSpinnerFactory(section, Objects::toString));
        p2.setPrefWidth(USE_COMPUTED_SIZE);
        p2.valueProperty().addListener((observable, oldValue, newValue) -> {
            title.setText(String.format("詩 %s 首", p1.getValue()));
            if (Objects.nonNull(p2.getValue()) && !p2.getValue().isEmpty() && !Objects.isNull(p1.getValue()) && !p1.getValue().isEmpty()) {
                loadHymn();
            }
        });
        p2.setOnScroll(UIHelper::mouseWheelHandler);
        final HBox controlBar = new HBox(new Text("詩"), p1, new Text("節"), p2);
        controlBar.setAlignment(Pos.BASELINE_LEFT);
        controlBar.setSpacing(10);
        controlBar.setPadding(new Insets(5));
        this.getChildren().addAll(title, textArea, controlBar);
    }

    private void loadHymn() {
        final String url = String.format("/hymn/H%s-%s.txt", p1.getValue(), p2.getValue());
        try {
            final String hymn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(url), StandardCharsets.UTF_8)).lines().collect(joining("\n"));
            textArea.setText(hymn);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }
}
