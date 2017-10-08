package exam.model;

import exam.BibleLoader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author steven
 */
public class Bible {

    private BibleLoader loader;

    public CompletableFuture<Void> init() {
        loader = new BibleLoader();
        return loader.init();
    }

    public String fetchVerbe(String canonShortName, int chapterNum, int verseNum) {

        return getCanon(canonShortName)
                .map(Canon::getChapters)
                .map(chapters -> chapters.get(chapterNum - 1))
                .map(Chapter::getVerses)
                .map(verses -> verses.get(verseNum - 1))
                .map(Verse::getText).orElse("");
    }

    public Optional<Canon> getCanon(String canonShortName) {
        return loader.getBible().stream().filter(canon -> Objects.equals(canon.getShortName(), canonShortName)).findFirst();
    }

    public Canon getCanon(int index) {
        return loader.getBible().get(index);
    }

    public List<Canon> getAllCanons() {
        return loader.getBible();
    }

    public Canon getByName(final String name) {
        return loader.getBible().stream().filter((Canon canon) -> {

            return Objects.equals(name, canon.getName());
        }).findFirst().get();
    }
}
