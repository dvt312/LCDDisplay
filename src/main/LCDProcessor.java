/*
 * Copyright(c) Daniel Veintimilla. 2016.
 */
package main;

import java.util.Scanner;

/**
 * Processes the user input and generates the LCD digits elements according to size and pattern.
 */
public class LCDProcessor
{
    /**
     * Gets the user input and process it generating the LCD pattern.
     */
    public void GenerateOutput()
    {
        boolean finish = false;
        Scanner scan = new Scanner(System.in);
        while(!finish)
        {
            String line = scan.next();
            if (line != null && !line.isEmpty())
            {
                line = line.trim();
                String[] parts = line.split(",");
                if (parts != null && parts.length == 2)
                {
                    // Validating input.
                    if (IsValid(parts[0])
                            && parts[1] != null
                            && !parts[1].isEmpty()
                            && parts[1].matches("[0-9]+")
                            && parts[1].length() > 0)
                    {
                        Process(Integer.parseInt(parts[0]), parts[1]);
                        finish = parts[0].equals("0") && parts[1].equals("0");
                    }
                    else
                    {
                        System.out.println("-----------------------------------------------------------");
                        System.out.println("----------------Input error, please fix it.----------------");
                        System.out.println("-----------------------------------------------------------");
                        finish = true;
                    }
                }
            }
        }
        scan.close();
    }

    /**
     * Process the input and generates the LCD digits output.
     *
     * @param size The digit size
     * @param pattern The pattern containing numbers to process
     */
    private void Process(int size, String pattern)
    {
        String[] patternArray = pattern.split("");
        if (patternArray != null && patternArray.length > 0)
        {
            String[][] dataMatrix = GenerateMatrix(size, patternArray.length);
            int column = 0;
            int row = 0;
            int tempRow = 0;
            int currentNumber = -1;
            String currentElement = "";
            int startingX = 0;
            boolean isLimit;
            for (int z = 0; z < patternArray.length; z++)
            {
                startingX = (size + 2) * z;
                column = startingX;
                row = 0;
                tempRow = 0;
                isLimit = false;
                currentNumber = -1;
                currentElement = "";
                currentNumber = Integer.parseInt(patternArray[z]);
                String[] numberArray = GetNumberArray(currentNumber);
                int maxRows = (2 * size) + 3;
                int maxColumns = size + 2;

                // Iterating the number array 
                for (int index = 0; index < numberArray.length; index++)
                {
                    isLimit = false;
                    currentElement = numberArray[index];

                    // When the element is in one of the corners or in the middle row.
                    if (IsLimit(column, maxColumns, row, maxRows, startingX))
                    {
                        dataMatrix[row][column] = " ";
                        column++;
                    }
                    // When the element is located first column or last column.
                    else if (IsLimitX(column, maxColumns, startingX))
                    {
                        isLimit = true;
                        tempRow = VerticalPaint(size, currentElement, column, row, dataMatrix);
                        column++;
                    }
                    // When the element is located in the first row, last row or middle row.
                    else if (IsLimitY(row, maxRows))
                    {
                        column = HorizontalPaint(size, currentElement, column, row, dataMatrix);
                    }
                    else
                    {
                        column = column + size;
                    }

                    // Changing row in the matrix.
                    if ((index + 1) % 3 == 0.0)
                    {
                        row = isLimit ? tempRow : row + 1;
                        column = startingX;
                    }
                }
            }
            PrintMatrix(dataMatrix);
        }
    }

    /**
     * Replicates the element to left, in the X axis.
     *
     * @param size The digit size
     * @param currentElement The current element to print
     * @param column The current column
     * @param row The current row
     * @param dataMatrix The matrix to insert value
     * @return The column value after be processed.
     */
    private int HorizontalPaint(
            int size, String currentElement, int column, int row, String[][] dataMatrix)
    {
        for (int sizeIndex = 0; sizeIndex < size; sizeIndex++)
        {
            dataMatrix[row][column] = currentElement;
            column++;
        }

        return column;
    }

    /**
     * Replicates the element below, in the Y axis.
     *
     * @param size The digit size
     * @param currentElement The current element to print
     * @param column The current column
     * @param row The current row
     * @param dataMatrix The matrix to insert value
     * @return The row value after be processed.
     */
    private int VerticalPaint(
            int size, String currentElement, int column, int row, String[][] dataMatrix)
    {
        for (int sizeIndex = 0; sizeIndex < size; sizeIndex++)
        {
            dataMatrix[row][column] = currentElement;
            row++;
        }

        return row;
    }

    /**
     * Checks if the current position is limit in X and Y axis.
     *
     * @param column The current column
     * @param maxColumns The max columns
     * @param row The current row
     * @param maxRows The max rows
     * @param startingColumn The starting column to process
     * @return A boolean indicating if the cursor is located in limit X and limit Y.
     */
    private boolean IsLimit(int column, int maxColumns, int row, int maxRows, int startingColumn)
    {
        return IsLimitX(column, maxColumns, startingColumn) && IsLimitY(row, maxRows);
    }

    /**
     * Checks if the current position is limit in axis.
     *
     * @param column The current column
     * @param maxColumns The max columns
     * @param startingColumn The starting column to process
     * @return A boolean indicating if the cursor is located in limit X.
     */
    private boolean IsLimitX(int column, int maxColumns, int startingColumn)
    {
        return column == startingColumn || column == startingColumn + maxColumns - 1;
    }

    /**
     * Checks if the current position is limit in Y axis.
     *
     * @param row The current row
     * @param maxRows The max rows
     * @return A boolean indicating if the cursor is located in limit Y.
     */
    private boolean IsLimitY(int row, int maxRows)
    {
        int middleRow = (int) Math.ceil((double)maxRows / 2);
        return row == 0 || row == maxRows - 1 || row == middleRow - 1;
    }

    /**
     * Prints the matrix.
     *
     * @param matrix The matrix to print
     */
    private void PrintMatrix(String[][] matrix)
    {
        for (int y = 0; y < matrix.length; y++)
        {
            for (int x = 0; x < matrix[y].length; x++)
            {
                System.out.print(matrix[y][x] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Generate an empty matrix.
     *
     * @param size The digit size
     * @param patternSize The pattern size
     * @return A brand new matrix object.
     */
    private String[][] GenerateMatrix(int size, int patternSize)
    {
        String[][] dataMatrix = new String[2*size + 3][];
        for (int y = 0; y < (2 * size) + 3; y++)
        {
            String[] dataRow = new String[(size + 2) * patternSize];
            for (int x = 0; x < (size + 2) * patternSize; x++)
            {
                dataRow[x] = " ";
            }
            dataMatrix[y] = dataRow;
        }

        return dataMatrix;
    }

    /**
     * Gets the number array according to the current number.
     *
     * @param number The current number 
     * @return The array associated to current number.
     */
    private String[] GetNumberArray(int number)
    {
        String[] array = null;

        switch (number)
        {
            case 1:
                array = LCDConstants.NUMBER_1;
                break;
            case 2:
                array = LCDConstants.NUMBER_2;
                break;
            case 3:
                array = LCDConstants.NUMBER_3;
                break;
            case 4:
                array = LCDConstants.NUMBER_4;
                break;
            case 5:
                array = LCDConstants.NUMBER_5;
                break;
            case 6:
                array = LCDConstants.NUMBER_6;
                break;
            case 7:
                array = LCDConstants.NUMBER_7;
                break;
            case 8:
                array = LCDConstants.NUMBER_8;
                break;
            case 9:
                array = LCDConstants.NUMBER_9;
                break;
            case 0:
                array = LCDConstants.NUMBER_0;
                break;
        }
        return array;
    }

    /**
     * Checks if the size input value is valid checking empty and if the value is a valid number.
     *
     * @param value The value to check
     * @return A boolean indicating if the value is valid or not
     */
    private boolean IsValid(String value)
    {
        try
        {
            return value != null && !value.isEmpty() && Integer.parseInt(value) > 0;
        }
        catch(NumberFormatException exception)
        {
            return false;
        }
    }
}
