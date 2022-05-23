/* 
 * CMSC 451 Project 1
 * Emily Williams
 * February 8, 2022
 * 
 * The MergeSortReport program allows the user to select a file that was
 * produced by the MergeSortBenchmark program which produces the report to be
 * displayed in a JTable. The report includes the data set size, average of the
 * critical counts for the 50 runs, the coefficient of variance of those 50
 * values, the average times and the coefficient of varience for times.
 */

package cmsc451project1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MergeSortReport extends JPanel {
    
    private File file;
    private Scanner scanLine;
    JLabel reportName;
    
    int size;
    int time = 0;
    int count = 0;
    int countTotal = 0;
    long timeTotal = 0;
    double timeSum = 0;
    double countSum = 0;
    double countDiff = 0;
    double timeDiff = 0;
    double sCountDiff = 0;
    double sTimeDiff = 0;
    double countDev = 0;
    double timeDev = 0;
    double countAvg;
    double timeAvg;
    String countCoef;
    String timeCoef;
    String[] columnNames = {"Size", "Avg Count", "Coef Count", 
        "Avg Time", "Coef Time"};
    
    // Create 2D array for report table data
    Object[][] data = {
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
        {size, countAvg, countCoef, timeAvg, timeCoef},
    };
    
    public void processReport() {
        // Create JFrame
        JFrame frame = new JFrame("Benchmark Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 350);
        // JLabel for report name
        JLabel name = new JLabel();
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        name.setHorizontalAlignment(JLabel.CENTER);
        panel.add(name, BorderLayout.PAGE_START);
        
        // Process File
        JFileChooser chooseFile = new JFileChooser("../Reports");
        int i = chooseFile.showOpenDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            File f = chooseFile.getSelectedFile();
            // Set name label based on file chosen
            if(f.getName().equals("iterative.txt")) {
                name.setText("Iterative MergeSort Benchmark Report");
            } else {
                name.setText("Recursive MergeSort Benchmark Report");
            }
            try {
                // Open and Scan Selected File
                Scanner scan = new Scanner(new FileReader(f));
                while (scan.hasNextLine()) {
                    // Data set size
                    data[i][0] = scan.nextInt();
                    String line = scan.nextLine();
                    scanLine = new Scanner(line);
                    // Scan file to get values of count and time
                    while (scanLine.hasNext()) {
                        if (scanLine.hasNextInt()){
                            countTotal = scanLine.nextInt();
                            countSum += countTotal;
                            count++;
                        } 
                        if (scanLine.hasNextLong()){
                            timeTotal = scanLine.nextLong();
                            timeSum += timeTotal;
                            time++;
                        }
                    }
                    // Calculate averages
                    countAvg = countSum/count;
                    timeAvg = timeSum/time;
                    // Assign averages to data array
                    data[i][1] = countAvg;
                    data[i][3] = timeAvg;
                    
                    scanLine = new Scanner(line);
                    
                    // Calculate coefficient of variation
                    // Variation: sum each (values - mean)^2 and divide by number of values
                    while(scanLine.hasNext()) {
                        if (scanLine.hasNextInt()){
                            countDiff = Math.pow((scanLine.nextInt() - countAvg), 2);
                            sCountDiff += countDiff;
                        } 
                        if (scanLine.hasNextLong()) {
                            timeDiff = Math.pow((scanLine.nextLong() - timeAvg), 2);
                            sTimeDiff += timeDiff;
                        }
                    }

                    // Variance
                    countDiff = sCountDiff/count;
                    timeDiff = sTimeDiff/time;
                    
                    // Standard Deviation (square root of variance)
                    countDev = Math.sqrt(countDiff);
                    timeDev = Math.sqrt(timeDiff);
                    
                    // Coefficient of Variation (standard deviation / average)
                    countCoef = String.format("%.2f", countDev/countAvg);
                    timeCoef = String.format("%.2f", timeDev/timeAvg);
                    
                    // Assign coef of variation to data array
                    data[i][2] = countCoef + "%";
                    data[i][4] = timeCoef + "%";
                    
                    // Reset counts to zero
                    count = 0;
                    time = 0;
                    countSum = 0;
                    timeSum = 0;
                    sCountDiff = 0;
                    sTimeDiff = 0;
                    
                    // Increment table assignment
                    i++;
                }
                scan.close();
                scanLine.close();
            } catch (IOException e) {
                System.err.println("Couldn't open the file " + file.getName());
                System.exit(1);
            }
        }
        
        // Create JTable for Data
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        // Create JScrollPane for Table
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);  
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        MergeSortReport report = new MergeSortReport();
        report.processReport();
    }
}

        
        