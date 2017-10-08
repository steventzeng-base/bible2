package exam;

import exam.model.Bible;
import exam.model.Canon;
import exam.model.Chapter;
import exam.model.Verse;
import exam.utils.UIHelper;
import java.util.Objects;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

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

    private Spinner<Chapter> chapter;

    private Spinner<Verse> verse;

    private Button goButton;

    private Button nextVerseButton;

    private Button saveButton;

    private Button biggerFont;

    private Button smallerFont;

    private Text verseShow;

    final ObservableList<Chapter> chapters = FXCollections.observableArrayList();

    final ObservableList<Verse> verses = FXCollections.observableArrayList();

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
                setMinHeight(38);
            }
        };
        final RowConstraints contentRowConstraints = new RowConstraints() {
            {
                setPrefHeight(Region.USE_COMPUTED_SIZE);
                setVgrow(Priority.ALWAYS);
            }
        };
        this.getStyleClass().add("ui");
        this.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        this.getRowConstraints().addAll(commonRowConstraints, commonRowConstraints, commonRowConstraints, contentRowConstraints, new RowConstraints(20), new RowConstraints(20));
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(5));
        title = new Text("title") {
            {
                getStyleClass().add("title");
            }
        };
        hymnal = new Text("hymnal") {
            {
                getStyleClass().add("title");
            }
        };
        speaker = new Text("speaker") {
            {
                getStyleClass().add("title");
            }
        };
        content = new TextArea();
        content.setWrapText(true);
        content.getStyleClass().add("kanban");
        this.add(title, 0, 0, 2, 1);
        this.add(new HBox(new Label("主領："), speaker) {
            {
                getStyleClass().add("title");
            }
        }, 0, 1);
        this.add(new HBox(new Label("詩歌："), hymnal) {
            {
                getStyleClass().add("title");
            }
        }, 1, 1);
        verseShow = new Text() {
            {
                getStyleClass().add("title");
            }
        };
        this.add(new HBox(verseShow), 0, 2, 2, 1);
        this.add(content, 0, 3, 2, 1);
        titleField = new TextField();
        titleField.setPromptText("題目");
        speakerField = new TextField();
        speakerField.setPromptText("主領人");
        hymnalField = new TextField();
        hymnalField.setPromptText("曲目");
        biggerFont = new Button("字大");
        smallerFont = new Button("字小");

        this.add(new HBox(titleField, speakerField, hymnalField, biggerFont, smallerFont) {
            {
                setSpacing(10);
                setPrefHeight(10);
            }
        }, 0, 4, 2, 1);
        canon = new ComboBox<Canon>() {
            {
                setPromptText("經卷");
                final Bible bible = new Bible();
                bible.init().thenRun(() -> {
                    getItems().addAll(bible.getAllCanons());
                    setCellFactory(param -> {
                        return new ListCell<Canon>() {

                            @Override
                            protected void updateItem(Canon item, boolean empty) {
                                super.updateItem(item, empty);
                                setText(Optional.ofNullable(item).map(Canon::getName).orElse(""));
                            }
                        };
                    });
                });
                setConverter(UIHelper.createStringConverter(Canon::getName));
            }
        };

        chapter = new Spinner<>(UIHelper.createSpinnerFactory(chapters, Chapter::getNum));
        chapter.setPrefWidth(USE_COMPUTED_SIZE);

        verse = new Spinner<>(UIHelper.createSpinnerFactory(verses, Verse::getNum));
        verse.setPrefWidth(USE_COMPUTED_SIZE);

        goButton = new Button("顯示");
        nextVerseButton = new Button("下一節");
        saveButton = new Button("存成 Word");
        this.add(new HBox(canon, chapter, new Text("章"), verse, new Text("節"), goButton, nextVerseButton, saveButton) {
            {
                setSpacing(10);
                setAlignment(Pos.BASELINE_LEFT);
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
        goButton.disableProperty().bind(verse.valueProperty().isNull());
        nextVerseButton.disableProperty().bind(verse.valueProperty().isNull());
        canon.valueProperty().addListener((observable, oldValue, newValue) -> {
            chapters.clear();
            if (Objects.nonNull(newValue)) {
                chapters.addAll(newValue.getChapters());
            }
        });
        chapter.valueProperty().addListener((observable, oldValue, newValue) -> {
            verses.clear();
            if (Objects.nonNull(newValue)) {
                verses.addAll(newValue.getVerses());
            }
        });
        chapter.setOnScroll(UIHelper::mouseWheelHandler);
        verse.setOnScroll(UIHelper::mouseWheelHandler);
        goButton.setOnAction(event -> {
            model.updateVerse();
            content.setScrollTop(Double.MAX_VALUE);
        });
        nextVerseButton.setOnAction(event -> {
            final Verse curVal = verse.getValue();
            verse.increment();
            final Verse nextVal = verse.getValue();
            if (Objects.equals(curVal, nextVal)) {
                return;
            }
            model.updateVerse();
            content.setScrollTop(Double.MAX_VALUE);
        });
        saveButton.setOnAction(event -> {
            model.saveFile(this.getScene().getWindow(), fileChooser);
        });
        biggerFont.setOnAction(event -> {
            final Font font = content.getFont();
            content.setStyle(String.format("-fx-font-size:%d", Math.min(((int) font.getSize() + 5), 80)));
        });
        smallerFont.setOnAction(event -> {
            final Font font = content.getFont();
            content.setStyle(String.format("-fx-font-size:%d", Math.max(((int) font.getSize() - 5), 20)));
        });
    }
}
