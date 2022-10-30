package com.example.qasystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class QASystemInterview2 {

    public static void main(String[] args) throws FileNotFoundException {

        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path filePath = Paths.get(root.toString(),"src", "main", "resources", "testQASystem.txt");

        File file = new File(filePath.toUri());
        Scanner scanner = new Scanner(file);

        LinkedList<String> lines = new LinkedList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();

        Iterator<String> iterator = lines.descendingIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}