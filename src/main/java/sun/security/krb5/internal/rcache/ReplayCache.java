/*
 * Copyright (c) 2000, 2006, Oracle and/or its affiliates. All rights reserved.
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

/*
 *
 *  (C) Copyright IBM Corp. 1999 All Rights Reserved.
 *  Copyright 1997 The Open Group Research Institute.  All rights reserved.
 */

package sun.security.krb5.internal.rcache;

import sun.security.krb5.internal.Krb5;
import java.util.LinkedList;
import java.util.ListIterator;
import sun.security.krb5.internal.KerberosTime;

/**
 * This class provides an efficient caching mechanism to store the timestamp of client authenticators.
 * The cache minimizes the memory usage by doing self-cleanup of expired items in the cache.
 *
 * @author Yanni Zhang
 */
public class ReplayCache extends LinkedList<AuthTime> {

    private static final long serialVersionUID = 2997933194993803994L;

    // These 3 fields are now useless, keep for serialization compatibility
    private String principal;
    private CacheTable table;
    private int nap = 10 * 60 * 1000; //10 minutes break

    private boolean DEBUG = Krb5.DEBUG;

    /**
     * Constructs a ReplayCache for a client principal in specified <code>CacheTable</code>.
     * @param p client principal name.
     * @param ct CacheTable.
     */
    public ReplayCache (String p, CacheTable ct) {
        principal = p;
        table = ct;
    }

    /**
     * Puts the authenticator timestamp into the cache in descending order.
     * @param t <code>AuthTime</code>
     */
    public synchronized void put(AuthTime t, long currentTime) {

        if (this.size() == 0) {
            addFirst(t);
        }
        else {
            AuthTime temp = getFirst();
            if (temp.kerberosTime < t.kerberosTime) {
                // in most cases, newly received authenticator has
                // larger timestamp.
                addFirst(t);
            }
            else if (temp.kerberosTime == t.kerberosTime) {
                if (temp.cusec < t.cusec) {
                    addFirst(t);
                }
            }
            else {
                //unless client clock being re-adjusted.
                ListIterator<AuthTime> it = listIterator(1);
                while (it.hasNext()) {
                    temp = it.next();
                    if (temp.kerberosTime < t.kerberosTime) {
                        add(indexOf(temp), t);
                        break;
                        //we always put the bigger timestamp at the front.
                    }
                    else if (temp.kerberosTime == t.kerberosTime) {
                        if (temp.cusec < t.cusec) {
                            add(indexOf(temp), t);
                            break;
                        }
                    }
                }
            }
        }

        // let us cleanup while we are here
        long timeLimit = currentTime - KerberosTime.getDefaultSkew() * 1000L;
        ListIterator<AuthTime> it = listIterator(0);
        AuthTime temp = null;
        int index = -1;
        while (it.hasNext()) {
            //search expired timestamps.
            temp = it.next();
            if (temp.kerberosTime < timeLimit) {
                index = indexOf(temp);
                break;
            }
        }
        if (index > -1) {
            do {
                //remove expired timestamps from the list.
                removeLast();
            } while(size() > index);
        }
        if (DEBUG) {
            printList();
        }
    }


    /**
     * Prints out the debug message.
     */
    private void printList() {
        Object[] total = toArray();
        for (int i = 0; i < total.length; i++) {
            System.out.println("object " + i + ": " + ((AuthTime)total[i]).kerberosTime + "/"
                               + ((AuthTime)total[i]).cusec);
        }
    }

}
