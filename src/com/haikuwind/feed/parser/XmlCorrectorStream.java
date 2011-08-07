package com.haikuwind.feed.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * HaikuWind server sends invalid XML for two cases:
 * <ol>
 * <li> unclosed haiku element in refresh response;
 * <li> haiku id without attribute (&lt;haiku="id" ...) for new haiku
 * </ol>
 * This InputStream must correct data for correct parsing.
 * 
 * It's not optimized and not multi-threading.
 * @author oakjumper
 *
 */
public class XmlCorrectorStream extends InputStream {
    private String buffer = "";
    private int currentPos = 0;
    
    private InputStream in;
    
    public XmlCorrectorStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        if(currentPos==buffer.length()) {
            refill();
        }
        if(buffer.length()==0) {
            return -1;
        }
        return buffer.charAt(currentPos++);
    }

    private void refill() throws IOException {
        StringBuilder builder = new StringBuilder();
        for(int c = in.read(); c!=-1; c = in.read()) {
            builder.append((char) c);
            if(c=='>') {
                break;
            }
        }
        buffer = builder.toString();
        buffer = buffer.replaceFirst("\\<haiku=", "<haiku id=");
        buffer = buffer.replaceFirst("(\\<haiku .*\")\\>", "$1/>");
        
        currentPos = 0;
    }
    
}
