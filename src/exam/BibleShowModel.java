/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exam;

import exam.model.Bible;
import exam.model.Canon;
import exam.model.Chapter;
import exam.model.DocGender;
import exam.model.Verse;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author steven
 */
public class BibleShowModel {

    private final Bible bible;

    private final StringProperty titlepProperty = new SimpleStringProperty();

    private final StringProperty speakerProperty = new SimpleStringProperty();

    private final StringProperty hymnalProperty = new SimpleStringProperty();

    private final StringProperty contentProperty = new SimpleStringProperty();

    private final SimpleObjectProperty<Canon> canon = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<Chapter> chapter = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<Verse> verse = new SimpleObjectProperty<>();

    private final StringProperty verseDescription = new SimpleStringProperty();

    public BibleShowModel() {
        bible = new Bible();
        bible.init();
    }

    public StringProperty titlepProperty() {
        return titlepProperty;
    }

    public StringProperty speakerProperty() {
        return speakerProperty;
    }

    public StringProperty hymnalProperty() {
        return hymnalProperty;
    }

    public StringProperty contentProperty() {
        return contentProperty;
    }

    public ObjectProperty<Canon> canonProperty() {
        return canon;
    }

    public ObjectProperty<Chapter> chapterProperty() {
        return chapter;
    }

    public ObjectProperty<Verse> verseProperty() {
        return verse;
    }

    public StringProperty verseDescriptionProperty() {
        return verseDescription;
    }

    public Bible getBible() {
        return bible;
    }

    public void updateVerse() {
        Optional.ofNullable(verse).ifPresent(v->{
            Verse data = v.get();
            contentProperty.set(String.format("%s%n %s %s", contentProperty.getValueSafe(),data.getShortVerse(), data.getText()));
            verseDescription.setValue(verse.get().getFullVerse());
        });
    }

    void saveFile(Window window, FileChooser fileChooser) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        String fileName = String.format("%s_%s_%s.docx", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), titlepProperty.get(), speakerProperty.get());
        fileName = fileName.replaceAll(" ", "");
        fileChooser.setInitialFileName(fileName);
        fileChooser.setTitle("儲存講道內容");
        final File file = fileChooser.showSaveDialog(window);
        DocGender gender = new DocGender();
        gender.setOutoutFile(file);
        gender.setContent(contentProperty.get());
        gender.setSpeaker(speakerProperty.get());
        gender.setTitle(titlepProperty.get());
        gender.setHymnal(hymnalProperty.get());
        gender.generate();
    }
}
