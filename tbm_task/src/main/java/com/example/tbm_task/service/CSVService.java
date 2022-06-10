package com.example.tbm_task.service;

import com.example.tbm_task.model.BTS;
import com.example.tbm_task.model.InfoBts;
import com.example.tbm_task.model.Result;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    InfoBtsService infoBtsService;

    public static String TYPE = "text/csv";

    public final static String[] HEADERs = {"Logic Id", "Time Up", "Time Down", "Duration(Formatted)", "Topology",
            "Device Name", "Additional Text", "# of Alarms", "# of Repeated", "Duration (Hours)", "Sum Duration", "Duration (Minutes)",
            "Duration (Seconds)"};

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<BTS> csvToBTS(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.EXCEL.withDelimiter(';').withHeader(HEADERs))) {
            List<BTS> allBts = new ArrayList<BTS>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                BTS bts = new BTS(
                        csvRecord.get("Logic Id"),
                        csvRecord.get("Time Up"),
                        csvRecord.get("Time Down"),
                        csvRecord.get("Duration(Formatted)"),
                        csvRecord.get("Topology"),
                        csvRecord.get("Device Name"),
                        csvRecord.get("Additional Text"),
                        csvRecord.get("# of Alarms"),
                        csvRecord.get("# of Repeated"),
                        csvRecord.get("Duration (Hours)"),
                        csvRecord.get("Sum Duration"),
                        csvRecord.get("Duration (Minutes)"),
                        csvRecord.get("Duration (Seconds)")
                );
                allBts.add(bts);
            }
            return allBts;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public List<Result> get(MultipartFile file) throws IOException {

        List<BTS> allBTS = csvToBTS(file.getInputStream());
        List<InfoBts> infoBts = infoBtsService.getInfoBts();

        List<Result> allInfo = new ArrayList<>();

        for (BTS bts : allBTS) {
            for (InfoBts infoBt : infoBts) {
                if (bts.getDevice_name().equals(infoBt.getId())) {
                    Result result = new Result(infoBt.getId(),
                            bts.getLogic_id(),
                            bts.getTime_up(),
                            bts.getTime_down(),
                            bts.getDuration(),
                            bts.getTopology(),
                            bts.getAdditional_info(),
                            infoBt.getLatitude(),
                            infoBt.getLongitude());
                    allInfo.add(result);
                    break;
                }
            }
        }

        Main main = new Main();
        main.getExcelFile(allInfo);

        return allInfo;
    }

    public void writeToCSV() throws IOException {


        final String[][] csvMatrix = new String[3][3];
        csvMatrix[0][0] = "first0";
        csvMatrix[0][1] = "second0";
        csvMatrix[0][2] = "third0";
        csvMatrix[1][0] = "first1";
        csvMatrix[1][1] = "second1";
        csvMatrix[1][2] = "third1";
        csvMatrix[2][0] = "first2";
        csvMatrix[2][1] = "second2";
        csvMatrix[2][2] = "third2";


        ICsvListWriter csvWriter = null;
        try {
            csvWriter = new CsvListWriter(new FileWriter("src/main/resources/files/employees.csv"),
                    CsvPreference.STANDARD_PREFERENCE);

            for (int i = 0; i < csvMatrix.length; i++) {
                csvWriter.write(csvMatrix[i]);
            }

        } catch (IOException e) {
            e.printStackTrace(); // TODO handle exception properly
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {

        CSVService csvService = new CSVService();
        csvService.writeToCSV();
    }

    public void saveDataToDB(MultipartFile file) {

        List<InfoBts> infoBtsList = getInfoBtsList(file);

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres", "postgres", "root123");

            PreparedStatement statement = con
                    .prepareStatement("INSERT INTO tbs_data VALUES(?, ?, ?)");
            int count = 0;
            for (InfoBts infoBts : infoBtsList) {

                statement.setString(1, infoBts.getId());
                statement.setString(2, infoBts.getLatitude());
                statement.setString(3, infoBts.getLongitude());

                statement.addBatch();
                count++;
                if (count % 100 == 0 || count == infoBtsList.size()) {
                    statement.executeBatch();
                }
            }

            statement.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private List<InfoBts> getInfoBtsList(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.EXCEL.withDelimiter(';').withHeader("№", "ID БС", "Широта", "Долгота", "Адрес обекта"))) {
            List<InfoBts> infoBtsList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                InfoBts infoBts = new InfoBts(
                        csvRecord.get("ID БС"),
                        csvRecord.get("Широта"),
                        csvRecord.get("Долгота")
                );
                infoBtsList.add(infoBts);
            }
            return infoBtsList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
