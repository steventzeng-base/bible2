package exam;

import exam.utils.PackageHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

/**
 *
 * @author steven
 */
public class HymnLoader {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final List<String> hymns = new ArrayList<>();
    private final Map<String, Integer> hymnCount = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        hymn();
    }

    private void hymn() {
        try {
            final ArrayList<String> hymnsWithVerse = Files.list(PackageHelper.getPackageContent("/hymn"))
                    .map(path -> path.getFileName().toString())
                    .map(line -> line.substring(1, line.indexOf('-')))
                    .sorted((v1, v2) -> Double.valueOf(v1).compareTo(Double.valueOf(v2)))
                    .collect(Collectors.toCollection(ArrayList::new));
            hymnsWithVerse.stream().forEach((hymnsNum) -> {
                int count = hymnCount.getOrDefault(hymnsNum, 0) +1;
                hymnCount.put(hymnsNum, count);
            });
            hymns.addAll(hymnsWithVerse);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public Set<String> getHymns() {
        return hymnCount.keySet();
    }
    
    public int getCount(String hymns){
        return hymnCount.get(hymns);
    }

}
