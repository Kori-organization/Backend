package com.example.koribackend.util;
import jakarta.servlet.ServletContext;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.HashSet;
import java.util.Set;

public class FileCpfManager {

    private ServletContext context;
    private String path = "/WEB-INF/data/cpf.txt";
    private InputStream file;
    private Set<String> cpfs = new HashSet<>();

    public FileCpfManager(ServletContext context) {
        this.context = context;
        file = context.getResourceAsStream(path);

        try (
                Scanner readFile = new Scanner(file)
        ) {
            while (readFile.hasNextLine()) {
                String line = readFile.nextLine().replaceAll("\\D","");
                cpfs.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkCpf(String targetCpf) {
        targetCpf = targetCpf.replaceAll("\\D","");
        return cpfs.contains(targetCpf);
    }

    public FileCpfManager deleteCpf(String cpf) {
        cpf = cpf.replaceAll("\\D","");
        cpfs.remove(cpf);
        return this;
    }

    public FileCpfManager addCpf(String cpf) {
        cpf = cpf.replaceAll("\\D","");
        cpfs.add(cpf);
        return this;
    }
}
