package core.view;

public interface ViewHandler {
    void process();

    default void throwable(Throwable throwable) {
        throwable.printStackTrace();
    }


}
