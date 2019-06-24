package Main;

public class TooManyRequestsException extends  Exception {
    public TooManyRequestsException()
    {
        super("Too many requests per second");
    }
}
