package exam;

import exam.model.Canon;
import exam.model.Chapter;
import exam.model.Verse;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

/**
 *
 * @author steven
 */
public class BibleShowGrid extends GridPane {

    private TextArea content;

    private Text title, hymnal, speaker;

    private TextField hymnalField, speakerField, titleField;

    private final BibleShowModel model = new BibleShowModel();

    private ComboBox<Canon> canon;

    private ComboBox<Chapter> chapter;

    private ComboBox<Verse> verse;

    private Button goButton;

    private Button saveButton;

    private Text verseShow;

    private final FileChooser fileChooser = new FileChooser();

    public BibleShowGrid() {
        this.render();
        this.binding();
    }

    protected void render() {
        final ColumnConstraints columnConstraints = new ColumnConstraints() {
            {
                setMinWidth(1024 / 2 - 25);
                setPercentWidth(90);
            }
        };
        final RowConstraints commonRowConstraints = new RowConstraints() {
            {
                setMinHeight(20);
            }
        };
        final RowConstraints contentRowConstraints = new RowConstraints() {
            {
                setPrefHeight(Region.USE_COMPUTED_SIZE);
                setVgrow(Priority.ALWAYS);
            }
        };
        this.setStyle("-fx-font:20pt monospace; -fx-font-smoothing-type:lcd;");
        this.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        this.getRowConstraints().addAll(commonRowConstraints, commonRowConstraints, commonRowConstraints, contentRowConstraints, commonRowConstraints, commonRowConstraints);
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(5));
        title = new Text("title");
        hymnal = new Text("hymnal");
        speaker = new Text("speaker");
        content = new TextArea();
        content.setWrapText(true);

        this.add(title, 0, 0, 2, 1);
        this.add(new HBox(new Label("主領："), speaker), 0, 1);
        this.add(new HBox(new Label("詩歌："), hymnal), 1, 1);
        verseShow = new Text();
        this.add(new HBox(verseShow), 0, 2, 2, 1);
        this.add(content, 0, 3, 2, 1);
        titleField = new TextField();
        titleField.setPromptText("題目");
        speakerField = new TextField();
        speakerField.setPromptText("主領人");
        hymnalField = new TextField();
        hymnalField.setPromptText("曲目");
        this.add(new HBox(titleField, speakerField, hymnalField) {
            {
                setSpacing(10);
                setPrefHeight(10);
                setStyle("-fx-font:12pt monospace; -fx-font-smoothing-type:lcd;");
            }
        }, 0, 4, 2, 1);
        canon = new ComboBox<>();
        canon.getItems().addAll(model.getBible().getAllCanons());
        canon.setCellFactory(param -> {
            return new ListCell<Canon>() {

                @Override
                protected void updateItem(Canon item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(Optional.ofNullable(item).map(Canon::getName).orElse(""));
                }
            };
        });
        canon.setConverter(new StringConverter<Canon>() {

            @Override
            public String toString(Canon canon) {
                return canon.getName();
            }

            @Override
            public Canon fromString(String name) {
                return model.getBible().getByName(name);
            }
        });

        chapter = new ComboBox<>();
        chapter.setPrefWidth(Region.USE_COMPUTED_SIZE);
        chapter.setCellFactory(param -> {

            return new ListCell<Chapter>() {

                @Override
                protected void updateItem(Chapter item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(Optional.ofNullable(item).map(Chapter::getNum).map(String::valueOf).orElse(""));
                }
            };
        });

        chapter.setConverter(new StringConverter<Chapter>() {

            @Override
            public String toString(Chapter chapter) {
                return String.valueOf(chapter.getNum());
            }

            @Override
            public Chapter fromString(String value) {
                return canon.getValue().getChapters().get(Integer.parseInt(value) - 1);
            }
        });

        verse = new ComboBox<>();
        verse.setPrefWidth(Region.USE_COMPUTED_SIZE);

        verse.setCellFactory(param -> {

            return new ListCell<Verse>() {

                @Override
                protected void updateItem(Verse item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(Optional.ofNullable(item).map(Verse::getNum).map(String::valueOf).orElse(""));
                }
            };
        });

        verse.setConverter(new StringConverter<Verse>() {

            @Override
            public String toString(Verse verse) {
                return String.valueOf(verse.getNum());
            }

            @Override
            public Verse fromString(String value) {
                return chapter.getValue().getVerses().get(Integer.parseInt(value) - 1);
            }
        });

        goButton = new Button("顯示");
        saveButton = new Button("存成 Word");
        this.add(new HBox(canon, chapter, new Text("章"), verse, new Text("節"), goButton, saveButton) {
            {
                setSpacing(10);
                setAlignment(Pos.BOTTOM_LEFT);
                setStyle("-fx-font:12pt monospace");
            }
        }, 0, 5, 2, 1);
    }

    protected void binding() {
        title.textProperty().bind(titleField.textProperty());
        speaker.textProperty().bind(speakerField.textProperty());
        hymnal.textProperty().bind(hymnalField.textProperty());
        model.canonProperty().bind(canon.valueProperty());
        model.chapterProperty().bind(chapter.valueProperty());
        model.verseProperty().bind(verse.valueProperty());
        model.titlepProperty().bind(titleField.textProperty());
        model.speakerProperty().bind(speakerField.textProperty());
        model.hymnalProperty().bind(hymnalField.textProperty());
        content.textProperty().bindBidirectional(model.contentProperty());
        verseShow.textProperty().bind(model.verseDescriptionProperty());
        canon.valueProperty().addListener((ObservableValue<? extends Canon> observable, Canon oldValue, Canon newValue) -> {
            chapter.setValue(null);
            verse.setValue(null);
            chapter.getItems().clear();
            verse.getItems().clear();
            chapter.getItems().addAll(canon.getValue().getChapters());
        });
        chapter.valueProperty().addListener((ObservableValue<? extends Chapter> ov, Chapter oldValue, Chapter newValue) -> {
            verse.setValue(null);
            verse.getItems().clear();
            Optional.ofNullable(chapter.getValue()).ifPresent((selectChapter) -> {
                verse.getItems().addAll(selectChapter.getVerses());
            });
        });
        goButton.setOnAction((event) -> {
            model.updateVerse();
            content.setScrollTop(Double.MAX_VALUE);
        });
        saveButton.setOnAction((event) -> {
            model.saveFile(this.getScene().getWindow(), fileChooser);
        });
    }
}
