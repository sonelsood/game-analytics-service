
package com.fsb.game.analytics.service.core.model;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/** Represents the context comprising of header values. */
@SuppressWarnings("unused")
public final class InvocationContext implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * {@link InheritableThreadLocal} context to store the invocation context.
     */
    private static final ThreadLocal<InvocationContext> THREADLOCAL = new InheritableThreadLocal<InvocationContext>() {

        /** {@inheritDoc} */
        protected InvocationContext initialValue() {
            return new InvocationContext();
        }
    };

    /** The trace id. */
    private long traceId;

    /** The span's id. */
    private long spanId;

    /** The span's parent span id. */
    private long parentSpanId;

    /** The user id. */
    private String userId;

    /** The user id. */
    private String providerId;

    private String httpMethodType;

    private String pathInfo;

    /** Constructor. */
    private InvocationContext() {
        // Prohibit instantiation.
    }

    /**
     * Compare this context with that context.
     * 
     * @param that The other context to compare with.
     * @return Returns the comparison result.
     */
    private boolean compareContexts(final InvocationContext that) {
        return Objects.equal(this.traceId + this.spanId + this.parentSpanId,
                that.traceId + that.spanId + that.parentSpanId) && Objects.equal(this.userId, that.userId)
                && Objects.equal(this.providerId, that.providerId);
    }

    /**
     * Gets the trace id.
     * 
     * @return Returns the trace id.
     */

    public static long getTraceId() {
        return get().traceId;
    }

    public static void setPathInfo(String pathInfo) {
        get().pathInfo = pathInfo;
    }

    /**
     * Sets the trace id.
     * 
     * 
     * @param newTraceId The trace id to set.
     */
    public static void setTraceId(long newTraceId) {
        get().traceId = newTraceId;
    }

    /**
     * Gets the span id.
     * 
     * @return Returns the span id.
     */
    public static long getSpanId() {
        return get().spanId;
    }

    public static void setHttpMethodType(String httpMethodType) {
        get().httpMethodType = httpMethodType;
    }

    /**
     * Sets the span id.
     * 
     * @param newSpanId The span id to set.
     */
    public static void setSpanId(long newSpanId) {
        get().spanId = newSpanId;
    }

    /**
     * Gets the parent span id.
     * 
     * @return Returns the parent span id.
     */
    public static long getParentSpanId() {
        return get().parentSpanId;
    }

    /**
     * Sets the parent span id.
     * 
     * @param newParentSpanId The parent span id to set.
     */
    public static void setParentSpanId(long newParentSpanId) {
        get().parentSpanId = newParentSpanId;
    }

    /**
     * @return the userId
     */
    public static String getUserId() {
        return get().userId;
    }

    /**
     * @param userId the userId to set
     */
    public static void setUserId(String userId) {
        get().userId = userId;
    }

    /**
     * @param providerId the providerId to set
     */
    public static void setProviderId(String providerId) {
        get().providerId = providerId;
    }

    /**
     * @return the providerId
     */
    public static String getProviderId() {
        return get().providerId;
    }

    /**
     * Sets the given invocation context on the current thread.
     * 
     * @param invocationContext The invocation context to set.
     */
    public static void set(InvocationContext invocationContext) {
        THREADLOCAL.set(invocationContext);
    }

    /** Clear the invocation context on the current thread. */
    public static void clear() {
        final InvocationContext invocationContext = get();
        if (invocationContext != null) {
            THREADLOCAL.remove();
        }
    }

    /**
     * Gets the current invocation context from the thread local context.
     * 
     * @return returns the InvocationContext object.
     */
    public static InvocationContext get() {
        return THREADLOCAL.get();
    }

    /** {@inheritDoc} */
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof InvocationContext)) {
            return false;
        }

        final InvocationContext that = (InvocationContext) obj;
        return compareContexts(that);
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return Objects.hashCode(traceId, spanId, parentSpanId, userId, providerId);
    }

    /** {@inheritDoc} */

    public String toString() {
        return MoreObjects.toStringHelper(this).add("traceId", traceId).add("spanId", spanId)
                .add("parentSpanId", parentSpanId).add("userId", userId).add("providerId", providerId).toString();
    }

}
