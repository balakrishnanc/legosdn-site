package edu.duke.cs.legosdn.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * ByteBufferBackedOutStream provides a simple output stream wrapped around a ByteBuffer.
 */
public class ByteBufferBackedOutStream extends OutputStream {

    private final ByteBuffer buffer;

    /**
     * Initialize ByteBufferBackedOutStream.
     *
     * @param buffer Byte buffer
     */
    public ByteBufferBackedOutStream(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public synchronized void write(int b) throws IOException {
        this.buffer.put((byte) b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.buffer.put(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.buffer.put(b, off, len);
    }

}
