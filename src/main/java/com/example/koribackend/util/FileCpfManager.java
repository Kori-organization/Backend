package com.example.koribackend.util;
import jakarta.servlet.ServletContext;

import java.io.*;
import java.util.Scanner;

import java.util.HashSet;
import java.util.Set;

// Utility class to manage CPF validation via a text file
public class FileCpfManager {

    private ServletContext context;
    private String path = "/WEB-INF/data/cpf.txt";
    private InputStream file;
    private Set<String> cpfs = new HashSet<>();

    // Constructor: loads CPFs from the file into a HashSet
    public FileCpfManager(ServletContext context) {
        this.context = context;
        file = context.getResourceAsStream(path);

        try (
                Scanner readFile = new Scanner(file)
        ) {
            while (readFile.hasNextLine()) {
                // Remove non-digits and add to the set
                String line = readFile.nextLine().replaceAll("\\D","");
                cpfs.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Checks if a specific CPF exists in the loaded set
    public boolean checkCpf(String targetCpf) {
        targetCpf = targetCpf.replaceAll("\\D","");
        return cpfs.contains(targetCpf);
    }

    // Removes a CPF from the set
    public FileCpfManager deleteCpf(String cpf) {
        cpf = cpf.replaceAll("\\D","");
        cpfs.remove(cpf);
        return this;
    }

    // Adds a new CPF to the set
    public FileCpfManager addCpf(String cpf) {
        cpf = cpf.replaceAll("\\D","");
        cpfs.add(cpf);
        return this;
    }
}