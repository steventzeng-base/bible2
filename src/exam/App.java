package exam;

import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 *
 * @author steven
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        final BibleShowGrid panel = new BibleShowGrid();
        Tab tab1 = new Tab("經節");
        Tab tab2 = new Tab("讚美詩");
        tab1.setContent(panel);
        tab2.setContent(new HymnPanel());
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.BOTTOM);
        final Scene scene = new Scene(tabPane, 1024, 768, false, SceneAntialiasing.BALANCED);
        scene.getStylesheets().add(this.getClass().getResource("/default.css").toExternalForm());
        tabPane.prefHeightProperty().bind(scene.heightProperty());
        tabPane.prefWidthProperty().bind(scene.widthProperty());
        stage.setScene(scene);
        stage.show();
    }
}
