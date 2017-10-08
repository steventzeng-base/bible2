package exam;

import exam.model.BibleLine;
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
import java.util.concurrent.CompletableFuture;
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

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final List<Canon> canons = new ArrayList<>();

    private final Pattern delimiter = Pattern.compile("\\|");

    @PostConstruct
    public CompletableFuture<Void> init() {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            final long start = System.nanoTime();
            final InputStreamReader reader = new InputStreamReader(BibleLoader.class.getResourceAsStream("/holy_bible.txt"), StandardCharsets.UTF_8);
            try (Stream<String> lines = new BufferedReader(reader).lines()) {
                lines.onClose(() -> LOGGER.info("file is close")).map(this::parseLine).forEach(this::buildBibileData);
            }
            LOGGER.log(Level.INFO, "total load time = {0}", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        });
        return completableFuture;
    }

    protected BibleLine parseLine(final String line) {
        final Scanner scanner = new Scanner(line).useDelimiter(delimiter);
        String canonName = scanner.next();
        String canonShortName = scanner.next();
        int chapterNum = scanner.nextInt();
        int verseNum = scanner.nextInt();
        int pageNum = scanner.nextInt();
        String text = scanner.next();
        return new BibleLine(canonName, canonShortName, chapterNum, verseNum, pageNum, text);
    }

    private void buildBibileData(BibleLine line) {
        final Canon curCanon = canons.stream().filter(c -> c.getShortName().equals(line.getCanonShortName()))
                .findFirst().orElseGet(() -> newCanon(line.getCanonName(), line.getCanonShortName()));
        final Chapter curChapter = curCanon.getChapters().stream().filter(ch -> ch.getNum() == line.getChapterNum())
                .findFirst().orElseGet(() -> newChapter(curCanon, line.getChapterNum()));
        curChapter.getVerses().add(new Verse(curChapter, line.getVerseNum(), line.getText(), line.getPageNum()));
    }

    private Canon newCanon(final String canonName, final String canonShortName) {
        final Canon canon = new Canon(canonName, canonShortName);
        canons.add(canon);
        return canon;
    }

    private Chapter newChapter(Canon canon, int chapterNum) {
        final Chapter chapter = new Chapter(canon, chapterNum);
        canon.getChapters().add(chapter);
        return chapter;
    }

    public List<Canon> getBible() {
        return Collections.unmodifiableList(canons);
    }
}
