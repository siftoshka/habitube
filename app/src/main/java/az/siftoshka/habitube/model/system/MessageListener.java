package az.siftoshka.habitube.model.system;

public interface MessageListener {

    void showInternetError(String message);
    void showText(String message);
}
