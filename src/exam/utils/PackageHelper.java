package exam.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author steven
 */
public class PackageHelper {

    public static Path getPackageContent(String name) {
        try {
            URI uri = PackageHelper.class.getClass().getResource(name).toURI();
            switch (uri.getScheme()) {
                case "file":
                    return Paths.get(uri);
                case "jar":
                    return FileSystems.newFileSystem(uri, new HashMap<>()).getPath(name);
                default:
                    throw new IllegalArgumentException();
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
