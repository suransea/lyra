package com.sea.pxxd.util;

import java.util.ArrayList;
import java.util.List;

public class ConsoleTable {

    private final static int MARGIN_WIDTH = 2;

    private List<List<Object>> rows = new ArrayList<>();
    private int columnCount;
    private int[] columnWidths;


    public ConsoleTable(int columnCount) {
        this.columnCount = columnCount;
        this.columnWidths = new int[columnCount];
    }

    public void appendRow() {
        List<Object> row = new ArrayList<>();
        rows.add(row);
    }

    public ConsoleTable appendColumn(Object value) {
        if (value == null) {
            value = "(null)";
        }
        List<Object> row = rows.get(rows.size() - 1);
        row.add(value);
        int width = value.toString().getBytes().length;
        if (columnWidths[row.size() - 1] < width) {
            columnWidths[row.size() - 1] = width;
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        //head
        result.append('┌');
        result.append(repeatChar('─', MARGIN_WIDTH * 2 + columnWidths[0]));
        for (int i = 1; i < columnCount; i++) {
            result.append('┬');
            result.append(repeatChar('─', MARGIN_WIDTH * 2 + columnWidths[i]));
        }
        result.append("┐\n");

        //head data
        for (int i = 0; i < columnCount; i++) {
            String item = "";
            if (i < rows.get(0).size()) {
                item = rows.get(0).get(i).toString();
            }
            result.append('│').append(repeatChar(' ', MARGIN_WIDTH)).append(item);
            result.append(repeatChar(' ', columnWidths[i] - item.getBytes().length + MARGIN_WIDTH));
        }
        result.append("│\n");

        //span
        result.append('├');
        result.append(repeatChar('─', MARGIN_WIDTH * 2 + columnWidths[0]));
        for (int i = 1; i < columnCount; i++) {
            result.append('┼');
            result.append(repeatChar('─', MARGIN_WIDTH * 2 + columnWidths[i]));
        }
        result.append("┤\n");

        //data lines
        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
            List row = rows.get(rowIndex);
            for (int i = 0; i < columnCount; i++) {
                String item = "";
                if (i < row.size()) {
                    item = row.get(i).toString();
                }
                result.append('│').append(repeatChar(' ', MARGIN_WIDTH)).append(item);
                result.append(repeatChar(' ', columnWidths[i] - item.getBytes().length + MARGIN_WIDTH));
            }
            result.append("│\n");
        }

        //tail
        result.append('└').append(repeatChar('─', MARGIN_WIDTH * 2 + columnWidths[0]));
        for (int i = 1; i < columnCount; i++) {
            result.append('┴');
            result.append(repeatChar('─', MARGIN_WIDTH * 2 + columnWidths[i]));
        }
        result.append("┘\n");
        result.append(rows.size() - 1).append(" rows in set.");
        return result.toString();
    }

    private String repeatChar(char c, int len) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            result.append(c);
        }
        return result.toString();
    }
}