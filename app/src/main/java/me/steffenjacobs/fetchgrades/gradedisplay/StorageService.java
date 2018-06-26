package me.steffenjacobs.fetchgrades.gradedisplay;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.steffenjacobs.fetchgrades.gradefetcher.Module;

/**
 * @author Steffen Jacobs
 */
public class StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageService.class);

    private static final String colExamNumber = "examNumber";
    private static final String colSemester = "semester";
    private static final String colExamDate = "examDate";
    private static final String colRound = "round";
    private static final String colModuleName = "moduleName";
    private static final String colExaminer = "examiner";
    private static final String colType = "type";
    private static final String colGrade = "grade";
    private static final String colECTS = "ects";
    private static final String colPassed = "passed";
    private static final String colAttempt = "attempt";

    public void store(List<Module> list, String filename, Context context) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE), Charset.forName("UTF-8"))),
                CSVFormat.RFC4180.withHeader(colExamNumber, colSemester, colExamDate, colRound, colModuleName, colExaminer, colType, colGrade, colECTS, colPassed, colAttempt));) {
            for (Module m : list) {
                printer.printRecord(toRecord(m));
            }
            printer.flush();
        } catch (IOException | IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public List<Module> load(String filename, Context context) throws IOException {
        CSVParser parser = CSVParser.parse(context.openFileInput(filename), Charset.forName("UTF-8"), CSVFormat.RFC4180.withHeader(colExamNumber, colSemester, colExamDate, colRound, colModuleName, colExaminer, colType, colGrade, colECTS, colPassed, colAttempt));
        List<Module> modules = new ArrayList<>();
        for (CSVRecord record : parser) {
            try {
                modules.add(parseModule(record));
            } catch (NumberFormatException | ParseException e) {
                LOG.error(e.getMessage());
            }
        }
        return modules;
    }

    private Iterable<String> toRecord(Module module) {
        ArrayList<String> record = new ArrayList<>();
        record.add(module.getExamNumber() + "");
        record.add(module.getSemester());
        record.add(Module.DATE_FORMAT.format(module.getExamDate()));
        record.add(module.getRound() + "");
        record.add(module.getModuleName());
        record.add(module.getExaminer());
        record.add(module.getExamType());
        record.add(module.getGrade() + "");
        record.add(module.getEcts() + "");
        record.add(module.isPassed() + "");
        record.add(module.getAttempt() + "");
        return record;
    }

    private Module parseModule(CSVRecord record) throws ParseException, NumberFormatException {
        int examNumber = Integer.parseInt(record.get(colExamNumber));
        String semester = record.get(colSemester);
        Date examDate = Module.DATE_FORMAT.parse(record.get(colExamDate));
        int round = Integer.parseInt(record.get(colRound));
        String moduleName = record.get(colModuleName);
        String examiner = record.get(colExaminer);
        String type = record.get(colType);
        double grade = Double.parseDouble(record.get(colGrade));
        int ects = Integer.parseInt(record.get(colECTS));
        boolean passed = Boolean.parseBoolean(record.get(colPassed));
        int attempt = Integer.parseInt(record.get(colAttempt));
        return new Module(examNumber, semester, examDate, round, moduleName, examiner, type, grade, ects, passed, attempt);

    }
}
