package com.raveline.stockmarketapplication.data.csv

import java.io.InputStream
/**
 * CSVParser is an interface for parsing CSV data.
 * It defines a single function, parse, which takes an InputStream of CSV data and returns a list of objects of type T.
 *
 * @param T The type of objects to be parsed from the CSV data.
 */
interface CSVParser<T> {

    /**
     * This function parses an InputStream of CSV data into a list of objects of type T.
     *
     * @param stream The InputStream containing the CSV data.
     * @return A list of objects of type T parsed from the CSV data.
     */
    suspend fun parse(stream: InputStream): List<T>
}