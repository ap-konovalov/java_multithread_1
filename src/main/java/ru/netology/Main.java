package ru.netology;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

// https://github.com/netology-code/jd-homeworks/blob/video/multithreading/task1/README.md
// https://github.com/netology-code/jd-homeworks/tree/video/multithreading/task2
public class Main {

    public static void main(String[] args) {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time

        List<FutureTask> threads = new ArrayList<>();
        for (String text : texts) {
            GetMaxCallable getMaxCallable = new GetMaxCallable(text);
            FutureTask futureTask = new FutureTask(getMaxCallable);
            threads.add(futureTask);
            new Thread(futureTask).start();
        }
        AtomicInteger maxFromAllStrings = new AtomicInteger();
        threads.forEach(t -> {
            try {
                Integer maxFromCurrentString = (Integer) t.get();
                if (maxFromCurrentString > maxFromAllStrings.get()){
                    maxFromAllStrings.set(maxFromCurrentString);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Max value: " + maxFromAllStrings);
        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}