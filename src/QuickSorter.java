import java.util.ArrayList;
import java.util.Random;

public class QuickSorter
{
    public enum PivotStrategy {FIRST_ELEMENT, RANDOM_ELEMENT, MEDIAN_OF_THREE_RANDOM_ELEMENTS, MEDIAN_OF_THREE}

    /**
     * Generates a list of a given size containing randomly generated integers
     * @param size - the size of the list to be generated
     * @return - a list of random integers
     * @throws IllegalArgumentException if the input size is negative
     */
    public static ArrayList<Integer> generateRandomList(int size)
    {
        //Throws an exception if the array size is negative
        if(size < 0)
        {
            throw new IllegalArgumentException();
        }

        ArrayList<Integer> randomList = new ArrayList<>();

        Random numberGenerator = new Random();

        //Loop to generate the random numbers
        for(int i = 0; i < size; i++)
        {
            //First, determines the sign of the value to be inserted, then determines the value
            if(numberGenerator.nextBoolean())
                randomList.add(i, -1 * numberGenerator.nextInt(Integer.MAX_VALUE));
            else
                randomList.add(i, numberGenerator.nextInt(Integer.MAX_VALUE));
        }

        return randomList;
    }

    /**
     * Sorts an ArrayList using Quicksort and then returns the time it took to run in nanoseconds.
     * Allows the client to manually select from four pivot options
     * @param inputList - the list to be sorted
     * @param strategy - the pivot selection strategy to be used
     * @param <E> - any Comparable object
     * @return - the time (in nanoseconds) the sorting took
     * @throws NullPointerException if either argument is null
     */
    public static <E extends Comparable<E>> long timedQuickSort(ArrayList<E> inputList, PivotStrategy strategy)
    {
        long startTime = java.lang.System.nanoTime();

        if(inputList == null || strategy == null)
        {
            throw new NullPointerException();
        }

        timedQuickSort(inputList, strategy, 0, inputList.size() - 1);

        return java.lang.System.nanoTime() - startTime;
    }

    //timedQuickSort function but with a starting and ending index to allow for everything to be done in place easily
    private static <E extends Comparable<E>> void timedQuickSort(ArrayList<E> inputList, PivotStrategy strategy, int startIndex, int endIndex)
    {
        if(endIndex - startIndex <= 20)
        {
            insertionSort(inputList, startIndex, endIndex);
        }
        else
        {
            int pivotIndex = getPivot(inputList, strategy, startIndex, endIndex);

            //Place values less than pivot to left of pivot, greater than pivot to right of pivot
            pivotIndex = balance(inputList, pivotIndex, startIndex, endIndex);

            //Perform quicksort on left and right side of array
            timedQuickSort(inputList, strategy, startIndex, pivotIndex - 1);
            timedQuickSort(inputList, strategy, pivotIndex + 1, endIndex);
        }
    }

    //Helper method. Used to perform insertion sort when subarray size is less than 20.
    //Sorts array from start index to end index, inclusive
    private static <E extends Comparable<E>> void insertionSort(ArrayList<E> inputList, int startIndex, int endIndex)
    {
        for(int i = startIndex; i < endIndex; i++)
        {
            int j = i + 1;
            while(j > startIndex && inputList.get(i + 1).compareTo(inputList.get(j - 1)) < 0)
            {
                j--;
            }

            inputList.add(j, inputList.remove(i + 1));
        }
    }

    //Helper method to balance values around the pivot (smaller values to left, larger values to right)
    //Returns the array index the pivot value ends up at
    private static <E extends Comparable<E>> int balance(ArrayList<E> inputList, int pivot, int startIndex, int endIndex)
    {
        //Check values that come before the pivot in the array
        for(int i = startIndex; i < pivot; i++)
        {
            if(inputList.get(i).compareTo(inputList.get(pivot)) > 0)
            {
                //If an element before the pivot is larger than the pivot, it is removed and placed at the end of the list
                inputList.add(endIndex, inputList.remove(i));

                //Adjusts pivot index to reflect change
                pivot--;
                i--;
            }
        }

        //Check values that come after the pivot
        for(int i = pivot + 1; i < endIndex; i++)
        {
            //If an element after the pivot is smaller than the pivot, remove and place at front of list
            if(inputList.get(i).compareTo(inputList.get(pivot)) < 0)
            {
                inputList.add(startIndex, inputList.remove(i));

                //Adjusts pivot index to reflect change
                pivot++;
                i--;
            }
        }

        return pivot;
    }

    /**
     * Copies a given arraylist
     * @param inputList - the list to be copied
     * @param <E> - the type of the arraylist. Must be a Comparable object
     * @return - an exact copy of the list
     */
    public static <E extends Comparable<E>> ArrayList<E> duplicateArray(ArrayList<E> inputList)
    {
        ArrayList<E> listCopy = new ArrayList<>(inputList.size());
        listCopy.addAll(inputList);
        return listCopy;
    }

    //Helper method used to find the pivot index based on the strategy
    private static <E extends Comparable<E>> int getPivot(ArrayList<E> inputList, PivotStrategy strategy, int startIndex, int endIndex)
    {
        switch (strategy)
        {
            case FIRST_ELEMENT: return startIndex;
            case RANDOM_ELEMENT: return (int) (Math.random() * (endIndex - startIndex + 1)) + startIndex;
            case MEDIAN_OF_THREE_RANDOM_ELEMENTS:
            {
                int firstElementIndex = (int) (Math.random() * (endIndex - startIndex + 1)) + startIndex;
                int secondElementIndex = (int) (Math.random() * (endIndex - startIndex + 1)) + startIndex;
                int thirdElementIndex = (int) (Math.random() * (endIndex - startIndex + 1)) + startIndex;

                return getMedianIndex(inputList, firstElementIndex, secondElementIndex, thirdElementIndex);

            }
            case MEDIAN_OF_THREE:
            {
                int middleIndex = ((endIndex - startIndex) / 2) + startIndex;
                return getMedianIndex(inputList, startIndex, middleIndex, endIndex);
            }
        }

        //Default return if an invalid pivotStrategy is given. Should never occur.
        return -1;
    }

    //Helper method to get the median of three elements in an arraylist given the indices of the elements
    private static <E extends Comparable<E>> int getMedianIndex(ArrayList<E> inputList, int firstElementIndex, int secondElementIndex, int thirdElementIndex)
    {
        ArrayList<E> medianFinderArray = new ArrayList<>();
        medianFinderArray.add(inputList.get(firstElementIndex));
        medianFinderArray.add(inputList.get(secondElementIndex));
        medianFinderArray.add(inputList.get(thirdElementIndex));
        insertionSort(medianFinderArray, 0,2);

        if(medianFinderArray.get(1).compareTo(inputList.get(firstElementIndex)) == 0)
            return firstElementIndex;
        else if(medianFinderArray.get(1).compareTo(inputList.get(secondElementIndex)) == 0)
            return secondElementIndex;
        return thirdElementIndex;
    }

}
