package com.example.qasystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QASystemInterview1 {
    private static final int BUFFER_SIZE = 8192;

    private final FileChannel channel;
    private final String encoding;
    private long filePos;
    private ByteBuffer buf;
    private int bufPos;
    private byte lastLineBreak = '\n';
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public QASystemInterview1(File file, String encoding) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        channel = raf.getChannel();
        filePos = raf.length();
        this.encoding = encoding;
    }

    public String readLine() throws IOException {
        while (true) {
            if (bufPos < 0) {
                if (filePos == 0) {
                    if (baos == null) {
                        return null;
                    }
                    String line = bufToString();
                    baos = null;
                    return line;
                }

                long start = Math.max(filePos - BUFFER_SIZE, 0);
                long end = filePos;
                long len = end - start;

                buf = channel.map(FileChannel.MapMode.READ_ONLY, start, len);
                bufPos = (int) len;
                filePos = start;
            }

            while (bufPos-- > 0) {
                byte c = buf.get(bufPos);
                if (c == '\r' || c == '\n') {
                    if (c != lastLineBreak) {
                        lastLineBreak = c;
                        continue;
                    }
                    lastLineBreak = c;
                    return bufToString();
                }
                baos.write(c);
            }
        }
    }

    private String bufToString() throws UnsupportedEncodingException {
        if (baos.size() == 0) {
            return "";
        }

        byte[] bytes = baos.toByteArray();
        for (int i = 0; i < bytes.length / 2; i++) {
            byte t = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = t;
        }

        baos.reset();

        return new String(bytes, encoding);
    }

    public static void main(String[] args) throws IOException {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path filePath = Paths.get(root.toString(),"src", "main", "resources", "testQASystem.txt");

        File file = new File(filePath.toUri());

        QASystemInterview1 reader = new QASystemInterview1(file, "UTF-8");
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
