package com.aliyuncs.fc.core;

import com.aliyuncs.fc.client.FcCallback;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureCallback<Req, Res> extends AbstractWatchableCallback<Req, Res> implements Future<Res> {
    private boolean completed = false;
    private Res result;
    private Exception ex;

    public void onCompleted(Req request, Res result) {
        synchronized(this) {
            if (this.completed) {
                throw new IllegalStateException("completed() must not be invoked twice.");
            }

            this.completed = true;
            this.result = result;
            this.notifyAll();
        }

        Iterator it = this.callbacks.iterator();

        while(it.hasNext()) {
            FcCallback<Req, Res> callback = (FcCallback)it.next();
            callback.onCompleted(request, result);
        }

    }

    public void onFailed(Req request, Exception ex) {
        synchronized(this) {
            if (this.completed) {
                throw new IllegalStateException("completed() must not be invoked twice.");
            }

            this.completed = true;
            this.ex = ex;
            this.notifyAll();
        }

        Iterator it = this.callbacks.iterator();

        while(it.hasNext()) {
            FcCallback<Req, Res> callback = (FcCallback)it.next();
            callback.onFailed(request, ex);
        }

    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        synchronized(this) {
            return this.completed;
        }
    }

    public synchronized Res get() throws InterruptedException {
        while(!this.completed) {
            this.wait();
        }

        return this.getResultWithoutLock();
    }

    public Res get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        Preconditions.checkNotNull(unit, "Time unit should not be null");
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        synchronized(this) {
            while(!this.completed) {
                long waitTime = endTime - System.currentTimeMillis();
                if (waitTime <= 0L) {
                    throw new TimeoutException();
                }

                this.wait(waitTime);
            }

            return this.getResultWithoutLock();
        }
    }

    private Res getResultWithoutLock() throws ClientException, ServerException {
        if (this.ex instanceof ServerException) {
            throw (ServerException)this.ex;
        } else if (this.ex instanceof ClientException) {
            throw (ClientException)this.ex;
        } else {
            return this.result;
        }
    }
}

