package exam;

import exam.model.Canon;
import exam.model.Chapter;
import exam.model.Verse;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
import javafx.scene.text.Font;
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

    private Button nextVerseButton;

    private Button saveButton;

    private Button biggerFont;

    private Button smallerFont;

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
        this.setStyle("-fx-font:20px monospace; -fx-font-smoothing-type:lcd;");
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
        biggerFont = new Button("字大");
        smallerFont = new Button("字小");

        this.add(new HBox(titleField, speakerField, hymnalField, biggerFont, smallerFont) {
            {
                setSpacing(10);
                setPrefHeight(10);
                setStyle("-fx-font:12px monospace; -fx-font-smoothing-type:lcd;");
            }
        }, 0, 4, 2, 1);
        canon = new ComboBox<Canon>() {
            {
                setPromptText("經卷");
                getItems().addAll(model.getBible().getAllCanons());
                setCellFactory(param -> {
                    return new ListCell<Canon>() {

                        @Override
                        protected void updateItem(Canon item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(Optional.ofNullable(item).map(Canon::getName).orElse(""));
                        }
                    };
                });
                setConverter(new StringConverter<Canon>() {

                    @Override
                    public String toString(Canon canon) {
                        return canon.getName();
                    }

                    @Override
                    public Canon fromString(String name) {
                        return model.getBible().getByName(name);
                    }
                });
            }
        };

        chapter = new ComboBox<Chapter>() {
            {
                setPromptText("章/篇");
                setPrefWidth(Region.USE_COMPUTED_SIZE);
                setCellFactory(param -> {
                    return new ListCell<Chapter>() {

                        @Override
                        protected void updateItem(Chapter item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(leafNodeStringValue(Chapter::getNum, item));
                        }
                    };
                });
                setConverter(new StringConverter<Chapter>() {

                    @Override
                    public String toString(Chapter chapter) {
                        return String.valueOf(chapter.getNum());
                    }

                    @Override
                    public Chapter fromString(String value) {
                        return canon.getValue().getChapters().get(Integer.parseInt(value) - 1);
                    }
                });
            }
        };

        verse = new ComboBox<Verse>() {
            {
                setPrefWidth(Region.USE_COMPUTED_SIZE);
                setPromptText("節");
                setCellFactory(param -> {

                    return new ListCell<Verse>() {
                        @Override
                        protected void updateItem(Verse item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(leafNodeStringValue(Verse::getNum, item));
                        }
                    };
                });
                setConverter(new StringConverter<Verse>() {

                    @Override
                    public String toString(Verse verse) {
                        return String.valueOf(verse.getNum());
                    }

                    @Override
                    public Verse fromString(String value) {
                        return chapter.getValue().getVerses().get(Integer.parseInt(value) - 1);
                    }
                });
            }
        };

        goButton = new Button("顯示");
        nextVerseButton = new Button("下一節");
        saveButton = new Button("存成 Word");
        this.add(new HBox(canon, chapter, verse, goButton, nextVerseButton, saveButton) {
            {
                setSpacing(10);
                setAlignment(Pos.BOTTOM_LEFT);
                setStyle("-fx-font:12pt monospace");
            }
        }, 0, 5, 2, 1);
    }

    <F, T> String leafNodeStringValue(Function<F, T> fun, F input) {
        return Optional.ofNullable(input).map(fun).map(Objects::toString).orElse("");
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
        verse.itemsProperty();
        canon.valueProperty().addListener((ObservableValue<? extends Canon> observable, Canon oldValue, Canon newValue) -> {
            chapter.setValue(null);
            chapter.getItems().clear();
            chapter.getItems().addAll(newValue.getChapters());
        });
        chapter.valueProperty().addListener((ov, oldValue, newValue) -> {
            verse.setValue(null);
            verse.getItems().clear();
            Optional.ofNullable(newValue).map(Chapter::getVerses).ifPresent(verse.getItems()::addAll);
        });
        goButton.setOnAction(event -> {
            model.updateVerse();
            content.setScrollTop(Double.MAX_VALUE);
        });
        nextVerseButton.setOnAction(event -> {
            final int nextIndex;
            if ((nextIndex = verse.getItems().indexOf(verse.getValue()) + 1) < verse.getItems().size()) {
                verse.setValue(verse.getItems().get(nextIndex));
                model.updateVerse();
                content.setScrollTop(Double.MAX_VALUE);
            }
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
