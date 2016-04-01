package edu.duke.cs.legosdn.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * ByteBufferBackedOutStream provides a simple input stream wrapped around a ByteBuffer.
 */
public class ByteBufferBackedInStream extends InputStream {

    private final ByteBuffer buffer;

    /**
     * Initialize ByteBufferBackedOutStream.
     *
     * @param buffer Byte buffer
     */
    public ByteBufferBackedInStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (!this.buffer.hasRemaining()) {
            return -1;
        }
        return this.buffer.get() & 0xFF;
    }

    @Override
    public int read(byte[] bytes, int off, int len) throws IOException {
        if (!this.buffer.hasRemaining()) {
            return -1;
        }

        len = Math.min(len, this.buffer.remaining());
        this.buffer.get(bytes, off, len);
        return len;
    }

}