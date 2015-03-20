package exam.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.util.StringConverter;

/**
 *
 * @author steven
 */
public class UIHelper {

    private UIHelper() {
    }

    public static void mouseWheelHandler(ScrollEvent event) {
        Spinner spinner = (Spinner) event.getSource();
        if (event.getDeltaY() > 0) {
            spinner.increment();
        } else {
            spinner.decrement();
        }
    }

    public static <T> SpinnerValueFactory.ListSpinnerValueFactory<T> createSpinnerFactory(ObservableList<T> observableList, Function<T, Object> showLabelFuntion) {
        SpinnerValueFactory.ListSpinnerValueFactory<T> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(observableList);
        valueFactory.setConverter(createStringConverter(showLabelFuntion));
        valueFactory.setWrapAround(true);
        return valueFactory;
    }

    public static <T> StringConverter<T> createStringConverter(Function<T, Object> showLabelFuntion) {
        StringConverter<T> converter = new StringConverter<T>() {

            @Override
            public String toString(T input) {
                return Optional.ofNullable(input).map(showLabelFuntion).map(Objects::toString).orElse("");
            }

            @Override
            public T fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        return converter;
    }
}
