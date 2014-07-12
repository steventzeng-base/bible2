package exam.model;

/**
 *
 * @author steven
 */
public class Verse {

    private final int pageNo;

    private final int num;

    private final String text;

    private final Chapter chapter;

    public Verse(final Chapter chapter, final int num, final String text, final int pageNo) {
        this.chapter = chapter;
        this.num = num;
        this.pageNo = pageNo;
        this.text = text;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public int getNum() {
        return num;
    }

    public String getText() {
        return text;
    }

    public int getPageNo() {
        return pageNo;
    }

    public String getFullVerse() {
        final Canon canon = chapter.getCanon();
        return String.format("%s 第 %d %s 第 %d 節 %d 頁", canon.getName(), chapter.getNum(), canon.getShortName().equals("詩") ? "篇" : "章", num, pageNo);
    }

    public String getShortVerse() {
        return String.format("%s%d:%d", chapter.getCanon().getShortName(), chapter.getNum(), num);
    }

}
