/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exam;

import exam.model.Canon;
import exam.model.Chapter;
import exam.model.Verse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;

/**
 *
 * @author steven
 */
public class BibleLoader {

    private final List<Canon> canos = new ArrayList<>();

    private Canon curCanon;

    private Chapter curChapter;

    private final Pattern delimiter = Pattern.compile("\\|");

    @PostConstruct
    public void init() {
        final long start = System.nanoTime();
        try (final Stream<String> lines = new BufferedReader(new InputStreamReader(BibleLoader.class.getResourceAsStream("/holy_bible.txt"), StandardCharsets.UTF_8)).lines().onClose(() -> {
            logger.info("file is close");
        })) {
            lines.forEach((line) -> {
                parseLine(line);
            });
        }
        
        logger.log(Level.INFO, "total load time = {0}", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
    }

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected void parseLine(final String line) {
        final Scanner scanner = new Scanner(line).useDelimiter(delimiter);
        String canonName = scanner.next();
        String canonShortName = scanner.next();
        int chapterNum = scanner.nextInt();
        int verseNum = scanner.nextInt();
        int pageNum = scanner.nextInt();
        String text = scanner.next();
        buildBibileData(canonName, canonShortName, chapterNum, verseNum, pageNum, text);
    }

    private void buildBibileData(String canonName, String canonShortName, int chapterNum, int verseNum, int pageNum, String text) {
        curCanon = curCanon != null && canonShortName.equals(curCanon.getShortName()) ? curCanon : newCanon(canonName, canonShortName);
        curChapter = !curCanon.getChapters().isEmpty() && curChapter.getNum() == chapterNum ? curChapter : newChapter(chapterNum);
        curChapter.getVerses().add(new Verse(curChapter, verseNum, text, pageNum));
    }

    private Canon newCanon(final String canonName, final String canonShortName) {
        final Canon canon = new Canon(canonName, canonShortName);
        canos.add(canon);
        return canon;
    }

    private Chapter newChapter(int chapterNum) {
        final Chapter chapter = new Chapter(curCanon, chapterNum);
        curCanon.getChapters().add(chapter);
        return chapter;
    }

    public List<Canon> getBible() {
        return Collections.unmodifiableList(canos);
    }
}
