import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        //Sample command line input: 100 report.txt unsorted.txt sorted.txt

        Scanner scan = new Scanner(System.in);

        String commandLineInput = scan.nextLine();

        //Input parsing
        int arraySize = Integer.parseInt(commandLineInput.substring(0, commandLineInput.indexOf(' ')));
        commandLineInput = commandLineInput.substring(commandLineInput.indexOf(' ') + 1);

        String reportFileName = commandLineInput.substring(0, commandLineInput.indexOf(' '));
        commandLineInput = commandLineInput.substring(commandLineInput.indexOf(' ') + 1);

        String unsortedArrayFileName = commandLineInput.substring(0, commandLineInput.indexOf(' '));
        commandLineInput = commandLineInput.substring(commandLineInput.indexOf(' ') + 1);

        //By now, the commandLineInput string should contain only the filename for the sorted array.

        PrintWriter unsortedArrayFile = new PrintWriter(unsortedArrayFileName);
        PrintWriter reportFile = new PrintWriter(reportFileName);
        PrintWriter sortedArrayFile = new PrintWriter(commandLineInput);

        //Writes unsorted array to one of the output files and closes that file
        ArrayList<Integer> unsortedArray = QuickSorter.generateRandomList(arraySize);
        unsortedArrayFile.println(unsortedArray);

        scan.close();
        unsortedArrayFile.close();

        //Run the various types of quicksort. Note that the array is duplicated to ensure that the sorting is performed
        //on an identical array each time
        ArrayList<Integer> listCopy = QuickSorter.duplicateArray(unsortedArray);
        long firstElementPivotSortTime = QuickSorter.timedQuickSort(listCopy, QuickSorter.PivotStrategy.FIRST_ELEMENT);

        listCopy = QuickSorter.duplicateArray(unsortedArray);
        long randomElementPivotSortTime = QuickSorter.timedQuickSort(listCopy, QuickSorter.PivotStrategy.RANDOM_ELEMENT);

        listCopy = QuickSorter.duplicateArray(unsortedArray);
        long randomMedianPivotSortTime = QuickSorter.timedQuickSort(listCopy, QuickSorter.PivotStrategy.MEDIAN_OF_THREE_RANDOM_ELEMENTS);

        long medianFirstMiddleLastPivotSortTime = QuickSorter.timedQuickSort(unsortedArray, QuickSorter.PivotStrategy.MEDIAN_OF_THREE);

        //Generate the report
        reportFile.println("Array Size: " + listCopy.size());
        reportFile.println("FIRST_ELEMENT : PT" + firstElementPivotSortTime * Math.pow(10, -9));
        reportFile.println("RANDOM_ELEMENT : PT" + randomElementPivotSortTime * Math.pow(10, -9));
        reportFile.println("MEDIAN_OF_THREE_RANDOM_ELEMENTS : PT" + randomMedianPivotSortTime * Math.pow(10, -9));
        reportFile.println("MEDIAN_OF_THREE_ELEMENTS : PT" + medianFirstMiddleLastPivotSortTime * Math.pow(10, -9));

        reportFile.close();

        //Write the sorted array to the sorted array output file
        sortedArrayFile.println(listCopy);
        sortedArrayFile.close();

    }
}
