package exam.model;

/**
 *
 * @author base
 */
public class BibleLine {

    private final String canonName;
    private final String canonShortName;
    private final int chapterNum;
    private final int verseNum;
    private final int pageNum;
    private final String text;

    public BibleLine(String canonName, String canonShortName, int chapterNum, int verseNum, int pageNum, String text) {
        this.canonName = canonName;
        this.canonShortName = canonShortName;
        this.chapterNum = chapterNum;
        this.verseNum = verseNum;
        this.pageNum = pageNum;
        this.text = text;
    }

    public String getCanonName() {
        return canonName;
    }

    public String getCanonShortName() {
        return canonShortName;
    }

    public int getChapterNum() {
        return chapterNum;
    }

    public int getVerseNum() {
        return verseNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public String getText() {
        return text;
    }

}
