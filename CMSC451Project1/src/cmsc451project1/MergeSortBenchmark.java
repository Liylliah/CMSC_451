/* 
 * CMSC 451 Project 1
 * Emily Williams
 * February 8, 2022
 * 
 * The MergeSortBenchmark program performs a benchmark of 50 data sets for each
 * value of n, and average the result of those 50 runs. It creates a set for a 
 * recursive and iterative algorithm. It outputs a space delimited text file for
 * each algorithm. It also performs a JVM warmup.
 */

package cmsc451project1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MergeSortBenchmark {
    private int counter = 0;
    private long startTime;
    private long endTime;
    
    /*
     * The iterative and recursive MergeSort algorithms are from 
     * https://tutorialspoint.dev/algorithm/sorting-algorithms/iterative-merge-sort
     * and were slightly modified.
     *
     * The iterative code was contributed by Smihta
     * The recursive code was contributed by Mohit Gupta_OMG
     */ 
    
    public void iterativeMerge(int[] array, int l, int m, int r) {
        int i, j, k; 
        int n1 = m - l + 1; 
        int n2 = r - m; 
      
        /* create temp arrays */
        int L[] = new int[n1]; 
        int R[] = new int[n2]; 
      
        /* Copy data to temp arrays L[] and R[] */
        for (i = 0; i < n1; i++) 
            L[i] = array[l + i]; 
        for (j = 0; j < n2; j++) 
            R[j] = array[m + 1 + j]; 
      
        /* Merge the temp arrays back into arr[l..r]*/
        i = 0; 
        j = 0; 
        k = l; 
        while (i < n1 && j < n2) { 
            if (L[i] <= R[j]) { 
                array[k] = L[i]; 
                counter++;
                i++; 
            } 
            else { 
                array[k] = R[j]; 
                counter++;
                j++; 
            } 
            k++; 
        } 
      
        /* Copy the remaining elements of L[], if there are any */
        while (i < n1) { 
            array[k] = L[i]; 
            counter++;
            i++; 
            k++; 
        } 
      
        /* Copy the remaining elements of R[], if there are any */
        while (j < n2) { 
            array[k] = R[j]; 
            counter++;
            j++; 
            k++; 
        } 
    } 
    
    
    public int[] iterativeSort(int[] array) {  
        // Start time for iterative sort
        this.startTime = System.nanoTime();
        
        // For current size of subarrays to be merged curr_size varies from  
        // 1 to n/2 
        int n = array.length;
        int curr_size;  
                      
        // For picking starting index of left subarray to be merged 
        int left_start; 

        // Merge subarrays in bottom up manner. First merge subarrays of size 1 
        // to create sorted subarrays of size 2, then merge subarrays of size 2 
        // to create sorted subarrays of size 4, and so on. 
        for (curr_size = 1; curr_size <= n-1; curr_size = 2*curr_size) {   
            // Pick starting point of different subarrays of current size 
            for (left_start = 0; left_start < n-1; left_start += 2*curr_size) { 
                // Find ending point of left subarray. mid+1 is starting  
                // point of right 
                int mid = Math.min(left_start + curr_size - 1, n-1); 
          
                int right_end = Math.min(left_start + 2*curr_size - 1, n-1); 
                
                // Merge Subarrays arr[left_start...mid] 
                // & arr[mid+1...right_end] 
                iterativeMerge(array, left_start, mid, right_end); 
            } 
        } 
        // End time for iterative sort
        this.endTime = System.nanoTime();
        
        // Check sort status of array
        try {
            checkSort(array);
        } catch (Exception e) {
            System.out.println("Array is not sorted.");
        }
        return array;
    } 
    
    
    public int[] recursiveMerge(int[] array) { 
        if(array == null) { 
            return array; 
        } 
  
        if(array.length > 1) { 
            int mid = array.length / 2; 
  
            // Split left part 
            int[] left = new int[mid]; 
            for(int i = 0; i < mid; i++) { 
                left[i] = array[i]; 
            } 
              
            // Split right part 
            int[] right = new int[array.length - mid]; 
            for(int i = mid; i < array.length; i++) { 
                right[i - mid] = array[i]; 
            } 
            recursiveMerge(left); 
            recursiveMerge(right); 
  
            int i = 0; 
            int j = 0; 
            int k = 0; 
  
            // Merge left and right arrays 
            while(i < left.length && j < right.length) { 
                if(left[i] < right[j]) { 
                    array[k] = left[i]; 
                    counter++;
                    i++; 
                } else { 
                    array[k] = right[j]; 
                    counter++;
                    j++; 
                } 
                k++; 
            } 
            // Collect remaining elements 
            while(i < left.length) { 
                array[k] = left[i]; 
                counter++;
                i++; 
                k++; 
            } 
            while(j < right.length) { 
                array[k] = right[j]; 
                counter++;
                j++; 
                k++; 
            } 
        } 
        // End time for recursive sort
        this.endTime = System.nanoTime();   
        // Check sort status of array
        try {
            checkSort(array);
        } catch (Exception e) {
            System.out.println("Array is not sorted.");
        }
        return array;
    } 
    
    public int[] recursiveSort(int[] array) {
        // Start time for recursive sort
        this.startTime = System.nanoTime();
        return recursiveMerge(array);
    }
    
    // Method to check if arrays are sorted
    public void checkSort(int[] array) throws Exception {
        boolean sorted;
        for(int i=0;i<array.length-1;i++) {
            //array is sorted 
            sorted = (array[i+1]>=array[i]);
            //array is not sorted
            if(!sorted) {
                throw new Exception();
            }
        }
    }
    
    public long getTime() {
        return endTime - startTime;
    }
    
    public int getCount() {
        return this.counter;
    }
    
    public static void genReport() throws IOException {
        // New iterative and recursive objects
        MergeSortBenchmark iterative = new MergeSortBenchmark();
        MergeSortBenchmark recursive = new MergeSortBenchmark();
        
        // Create "Reports" folder a level up
        Files.createDirectories(Paths.get("../Reports"));
        
        // Create new text files
        File iterativeFile = new File("../Reports/iterative.txt");
        File recursiveFile = new File("../Reports/recursive.txt");
        BufferedWriter iterativeReport = new BufferedWriter(new FileWriter(iterativeFile));
        BufferedWriter recursiveReport = new BufferedWriter(new FileWriter(recursiveFile));
        
        // Data set size; 10 elements evenly spaced from 250 to 1375
        for (int i = 250; i <= 1375; i+=125) {
            iterativeReport.write(i + " ");
            recursiveReport.write(i + " ");
            // New int array
            int[] array = new int[i];
            // Each set of data will run fifty times
            for (int j = 0; j < 50; j++) {
                // Generate a random number each loop to fill aray
                for (int n = 0; n < array.length; n++) {
                    array[n] = (int)(Math.random() * 3000);
                }
                // Call methods to sort
                iterative.iterativeSort(array);
                recursive.recursiveSort(array);
                // Write count and time to files
                iterativeReport.write(iterative.getCount() + " " + iterative.getTime() + " ");
                recursiveReport.write(recursive.getCount() + " " + recursive.getTime() + " ");
                // Set counters to zero
                iterative.counter = 0;
                recursive.counter = 0;
            }
            iterativeReport.write("\n");
            recursiveReport.write("\n");
        }
        iterativeReport.close();
        recursiveReport.close();
    }
    
    
    // Warm Up the JVM
    public static void warmUp() {
       final int warmup = 100000;
       int[] array = new int[20];
       for(int i = 0; i < warmup; i++){
            MergeSortBenchmark rec = new MergeSortBenchmark();
            MergeSortBenchmark iter = new MergeSortBenchmark();
            for(int j = 0; j < array.length; j++) {
                array[j] = (int)(Math.random() * 100 + 1);
           }
            rec.recursiveSort(array);
            iter.iterativeSort(array);
       }
    }
    
    
    public static void main(String[] args) throws IOException, Exception {
        warmUp();
        genReport();  
    } 
}
