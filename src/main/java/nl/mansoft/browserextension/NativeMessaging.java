package nl.mansoft.browserextension;

import java.io.IOException;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

/**
 *
 * @author Henri Manson
 */
public class NativeMessaging {
    public static int readInt32() throws IOException {
        int byte1 = System.in.read();
        if (byte1 == -1) {
            return -1;
        }
        int byte2 = System.in.read() << 8;
        if (byte2 == -1) {
            return -1;
        }
        int byte3 = System.in.read() << 16;
        if (byte3 == -1) {
            return -1;
        }
        int byte4 = System.in.read() << 24;
        if (byte4 == -1) {
            return -1;
        }
        return byte1 | byte2 | byte3 | byte4;
    }

    public static void writeInt32(int length) throws IOException {
        System.out.write(length);
        System.out.write(length >> 8);
        System.out.write(length >> 16);
        System.out.write(length >> 24);
    }

    public JsonObject readMessage() throws IOException {
        int len;
        JsonObject jsonObject = null;
        if ((len = readInt32()) != -1) {
            byte[] message = new byte[len];
            System.in.read(message);
            String str = new String(message, "UTF-8");
            StringReader reader = new StringReader(str);
            JsonParser parser = Json.createParser(reader);
            parser.next();
            jsonObject = parser.getObject();
        }
        return jsonObject;
    }

    public void writeMessage(JsonObject outputJson) throws IOException {
        String output = outputJson.toString();
        writeInt32(output.length());
        System.out.print(output);
        System.out.flush();
    }
}
