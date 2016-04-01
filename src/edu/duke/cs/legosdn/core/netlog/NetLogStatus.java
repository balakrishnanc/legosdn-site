package edu.duke.cs.legosdn.core.netlog;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * NetLogStatus is a response from NetLog indicating whether the transaction can proceed or not.
 */
public class NetLogStatus implements NetLogMsgIO {

    // True, if transaction can proceed; false, otherwise
    private boolean ok;

    /**
     * Initialize NetLogInitiate with no state.
     */
    public NetLogStatus() {
        this(false);
    }

    /**
     * Initialize NetLogInitiate.
     *
     * @param ok true, if transaction can proceed; false, otherwise
     */
    public NetLogStatus(final boolean ok) {
        this.ok = ok;
    }

    /**
     * @return true, if transaction can proceed; false, otherwise.
     */
    public boolean isOk() {
        return ok;
    }

    @Override
    public void writeTo(ObjectOutput out) throws IOException {
        out.writeBoolean(this.ok);
    }

    @Override
    public void readFrom(ObjectInput in) throws IOException, ClassNotFoundException {
        this.ok = in.readBoolean();
    }

    @Override
    public String toString() {
        if (!this.ok) {
            return "NetLogStatus::FAIL";
        }

        return "NetLogStatus::OK";
    }

}
