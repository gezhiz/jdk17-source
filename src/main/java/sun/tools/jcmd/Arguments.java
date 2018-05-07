/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
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

package sun.tools.jcmd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Arguments {
    private boolean listProcesses = false;
    private boolean listCounters  = false;
    private boolean showUsage     = false;
    private int     pid           = -1;
    private String  command       = null;
    private String  processSubstring;

    public boolean isListProcesses() { return listProcesses; }
    public boolean isListCounters() { return listCounters; }
    public boolean isShowUsage() { return showUsage; }
    public int getPid() { return pid; }
    public String getCommand() { return command; }
    public String getProcessSubstring() { return processSubstring; }

    public Arguments(String[] args) {
        if (args.length == 0 || args[0].equals("-l")) {
            listProcesses = true;
            return;
        }

        if (args[0].equals("-h") || args[0].equals("-help") ) {
            showUsage = true;
            return;
        }

        try {
            pid = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            // use as a partial class-name instead
            if (args[0].charAt(0) != '-') {
                // unless it starts with a '-'
                processSubstring = args[0];
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-f")) {
                if (args.length == i + 1) {
                    throw new IllegalArgumentException(
                        "No file specified for parameter -f");
                } else if (args.length == i + 2) {
                    try {
                        readCommandFile(args[i + 1]);
                    } catch(IOException e) {
                        throw new IllegalArgumentException(
                            "Could not read from file specified with -f option: "
                            + args[i + 1]);
                    }
                    return;
                } else {
                    throw new IllegalArgumentException(
                        "Options after -f are not allowed");
                }
            } else if (args[i].equals("PerfCounter.print")) {
                listCounters = true;
            } else {
                sb.append(args[i]).append(" ");
            }
        }

        if (listCounters != true && sb.length() == 0) {
            throw new IllegalArgumentException("No command specified");
        }

        command = sb.toString().trim();
    }

    private void readCommandFile(String path) throws IOException {
        try (BufferedReader bf = new BufferedReader(new FileReader(path));) {
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = bf.readLine()) != null) {
                    sb.append(s).append("\n");
                }
                command = sb.toString();
            }
    }

    public static void usage() {
        System.out.println("Usage: jcmd <pid | main class> <command ...|PerfCounter.print|-f file>");
        System.out.println("   or: jcmd -l                                                    ");
        System.out.println("   or: jcmd -h                                                    ");
        System.out.println("                                                                  ");
        System.out.println("  command must be a valid jcmd command for the selected jvm.      ");
        System.out.println("  Use the command \"help\" to see which commands are available.   ");
        System.out.println("  If the pid is 0, commands will be sent to all Java processes.   ");
        System.out.println("  The main class argument will be used to match (either partially ");
        System.out.println("  or fully) the class used to start Java.                         ");
        System.out.println("  If no options are given, lists Java processes (same as -p).     ");
        System.out.println("                                                                  ");
        System.out.println("  PerfCounter.print display the counters exposed by this process  ");
        System.out.println("  -f  read and execute commands from the file                     ");
        System.out.println("  -l  list JVM processes on the local machine                     ");
        System.out.println("  -h  this help                                                   ");
    }
}