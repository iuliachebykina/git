package com.company;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class Main {

    private static final ProcessBuilder processBuilder = new ProcessBuilder();

    public static void main(String[] args) throws IOException, InterruptedException {
        var url = "https://gitlab.com/iuliachebykina/skb-lab";
        var urls = url.split("/");
        var projectName = urls[urls.length-1];
        var clone = "git clone " + url + ".git";
        var repoDir = new File(new File("").getAbsolutePath()).getParent();
        var branch = "expWebhook"; // из мр достать
        var changeBranch = "git checkout " + branch;
        var projectDir = repoDir + "/" + projectName;
        HashMap<String, Integer> map = new HashMap<>();
        var script = new File("src/com/company/script.sh").getAbsolutePath();

        clonerepo(clone, repoDir);
        changeBranch(changeBranch, projectDir);
        getStatistics(script, projectDir, map);
    }



    static void clonerepo(String clone, String repoDir) throws IOException, InterruptedException {
        processBuilder.command("sh", "-c",clone);
        processBuilder.directory(new File(repoDir));
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }

    static void changeBranch(String changeBranch, String projectDir) throws IOException, InterruptedException {
        processBuilder.command("sh", "-c", changeBranch);
        processBuilder.directory(new File(projectDir));
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }

    static void getStatistics(String script, String projectDir, HashMap<String, Integer> map) throws InterruptedException, IOException {

        processBuilder.command(script);
        processBuilder.directory(new File(projectDir));
        Process process = processBuilder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), t->{
                    var arr = t.split(" ");
                    var user = arr[0];
                    var contr =Integer.parseInt(arr[1]);
                    map.put(user, contr);
                });
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        map.forEach((k, v) -> System.out.println(k + " " + v));
        assert exitCode == 0;
    }






}

