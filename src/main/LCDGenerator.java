/*
 * Copyright(c) Daniel Veintimilla. 2016.
 */
package main;

/**
 * Generates an LCD 7 segments representation according to input.
 */
public class LCDGenerator
{
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args)
    {
        LCDProcessor processor = new LCDProcessor();
        processor.GenerateOutput();
    }
}
