/*
 * Copyright (c) 1997, 2010, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.xml.internal.ws.client.sei;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Headers;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.MEP;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.*;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.SOAPSEIModel;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Stub} that handles method invocations
 * through a strongly-typed endpoint interface.
 *
 * @author Kohsuke Kawaguchi
 */
public final class SEIStub extends Stub implements InvocationHandler {

    @Deprecated
    public SEIStub(WSServiceDelegate owner, BindingImpl binding, SOAPSEIModel seiModel, Tube master, WSEndpointReference epr) {
        super(owner, master, binding, seiModel.getPort(), seiModel.getPort().getAddress(), epr);
        this.seiModel = seiModel;
        this.soapVersion = binding.getSOAPVersion();
        initMethodHandlers();
    }

    public SEIStub(WSPortInfo portInfo, BindingImpl binding, SOAPSEIModel seiModel, WSEndpointReference epr) {
        super(portInfo, binding, seiModel.getPort().getAddress(),epr);
        this.seiModel = seiModel;
        this.soapVersion = binding.getSOAPVersion();
        initMethodHandlers();
    }

    private void initMethodHandlers() {
        Map<WSDLBoundOperation, JavaMethodImpl> syncs = new HashMap<WSDLBoundOperation, JavaMethodImpl>();

        // fill in methodHandlers.
        // first fill in sychronized versions
        for (JavaMethodImpl m : seiModel.getJavaMethods()) {
            if (!m.getMEP().isAsync) {
                SyncMethodHandler handler = new SyncMethodHandler(this, m);
                syncs.put(m.getOperation(), m);
                methodHandlers.put(m.getMethod(), handler);
            }
        }

        for (JavaMethodImpl jm : seiModel.getJavaMethods()) {
            JavaMethodImpl sync = syncs.get(jm.getOperation());
            if (jm.getMEP() == MEP.ASYNC_CALLBACK) {
                Method m = jm.getMethod();
                CallbackMethodHandler handler = new CallbackMethodHandler(
                        this, jm, sync, m.getParameterTypes().length - 1);
                methodHandlers.put(m, handler);
            }
            if (jm.getMEP() == MEP.ASYNC_POLL) {
                Method m = jm.getMethod();
                PollingMethodHandler handler = new PollingMethodHandler(this, jm, sync);
                methodHandlers.put(m, handler);
            }
        }
    }

    public final SOAPSEIModel seiModel;

    public final SOAPVersion soapVersion;

    /**
     * Nullable when there is no associated WSDL Model
     * @return
     */
    public @Nullable
    OperationDispatcher getOperationDispatcher() {
        if(operationDispatcher == null && wsdlPort != null)
            operationDispatcher = new OperationDispatcher(wsdlPort,binding,seiModel);
        return operationDispatcher;
    }

    /**
     * For each method on the port interface we have
     * a {@link MethodHandler} that processes it.
     */
    private final Map<Method, MethodHandler> methodHandlers = new HashMap<Method, MethodHandler>();

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodHandler handler = methodHandlers.get(method);
        if (handler != null) {
            return handler.invoke(proxy, args);
        } else {
            // we handle the other method invocations by ourselves
            try {
                return method.invoke(this, args);
            } catch (IllegalAccessException e) {
                // impossible
                throw new AssertionError(e);
            } catch (IllegalArgumentException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    public final Packet doProcess(Packet request, RequestContext rc, ResponseContextReceiver receiver) {
        return super.process(request, rc, receiver);
    }

    public final void doProcessAsync(Packet request, RequestContext rc, Fiber.CompletionCallback callback) {
        super.processAsync(request, rc, callback);
    }

    protected final @NotNull QName getPortName() {
        return wsdlPort.getName();
    }


    public void setOutboundHeaders(Object... headers) {
        if(headers==null)
            throw new IllegalArgumentException();
        Header[] hl = new Header[headers.length];
        for( int i=0; i<hl.length; i++ ) {
            if(headers[i]==null)
                throw new IllegalArgumentException();
            hl[i] = Headers.create(seiModel.getJAXBContext(),headers[i]);
        }
        super.setOutboundHeaders(hl);
    }
}
