package lucagrazioli.popularmovies;

/**
 * Created by lucagrazioli on 19/08/15.
 */
public class NullJSONStringException extends Exception{
    public NullJSONStringException() {
        super();
    }

    public NullJSONStringException(String detailMessage) {
        super(detailMessage);
    }
}
