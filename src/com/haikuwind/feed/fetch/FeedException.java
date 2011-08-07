package com.haikuwind.feed.fetch;

public class FeedException extends Exception {

    private static final long serialVersionUID = -2510234090682625963L;

    public FeedException() {
        super();
    }

    public FeedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public FeedException(String detailMessage) {
        super(detailMessage);
    }

    public FeedException(Throwable throwable) {
        super(throwable);
    }

}
