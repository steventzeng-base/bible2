package exam.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;

import java.util.logging.Logger;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author steven
 */
public class DocGender {

    private String title;

    private String speaker;

    private String content;

    private String hymnal;

    private File outoutFile;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOutoutFile(File outoutFile) {
        this.outoutFile = outoutFile;
    }

    public void setHymnal(String hymnal) {
        this.hymnal = hymnal;
    }

    public void generate() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        final XWPFRun titleRun = paragraph.createRun();
        titleRun.setText(String.format("%s  詩 %s 首", title, hymnal));
        titleRun.setFontSize(18);
        paragraph = document.createParagraph();
        XWPFRun speakerRun = paragraph.createRun();
        speakerRun.setText(speaker);
        XWPFRun dateRun = paragraph.createRun();
        dateRun.setText("        " + new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        paragraph.setBorderBottom(Borders.BASIC_WIDE_INLINE);
        buildContent(document);
        try (FileOutputStream outputStream = new FileOutputStream(outoutFile)) {
            document.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, e.getMessage(), e);
        }
    }

    private void buildContent(XWPFDocument document) {
        Scanner scanner = new Scanner(content);
        scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            final String line = scanner.next();
            document.createParagraph().createRun().setText(line);
        }
    }
}
