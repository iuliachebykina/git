package com.company;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
	// write your code here
        test();
    }
    static void test() throws InterruptedException, IOException {
        HashMap<String, Integer> map = new HashMap<>();
        var script = "/home/iulia/IdeaProjects/git/src/com/company/script.sh";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", script);
        processBuilder.directory(new File("/home/iulia/IdeaProjects/skb-lab/.git"));
        Process process = processBuilder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), t->{
                    var arr = t.split(" ");
                    var user = arr[0];
                    var contr =Integer.parseInt(arr[1]);
                    map.put(user, contr);
                    System.out.println(user + " " + contr);
                });
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}
