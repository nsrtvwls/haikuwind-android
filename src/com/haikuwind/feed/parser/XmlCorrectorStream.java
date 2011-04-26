package com.haikuwind.feed.parser;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

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
        
        Log.d("XmlCorrectorStream", buffer);
        currentPos = 0;
    }
    
//    public static void main(String[] args) throws IOException {
//        String s = "<haiku text=\"%22come+fly+with+me%22+asked%0Ame+a+sparrow.+%22I+can%27t%22+said%0AI.+%22that%27s+what+you+think%22...\" id=\"2238105\" points=\"0\" user=\"60e06ae74feeb0d23547c2eec893b59f2cf2b151\" userRank=\"7\" favoritedByMe=\"false\" timesVotedByMe=\"0\" time=\"1303783979745\">";
//        String s = "<haiku=\"1303783979745\"/>";
//        System.out.println(s);
//            
//        XmlCorrectorStream corrector = new XmlCorrectorStream(new ByteArrayInputStream(s.getBytes()));
//        for(int i=0; i<53; ++i) {
//            corrector.refill();
//            System.out.print(corrector.buffer);
//        }
//    }
}
