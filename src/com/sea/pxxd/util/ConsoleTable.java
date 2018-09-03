package com.sea.pxxd.util;

import java.util.ArrayList;
import java.util.List;

public class ConsoleTable {
    private List<List<Object>> rows = new ArrayList<>();

    private int column;

    private int[] columnLength;

    private static int margin = 2;

    public ConsoleTable(int column) {
        this.column = column;
        this.columnLength = new int[column];
    }

    public void appendRow() {
        List<Object> row = new ArrayList<>(column);
        rows.add(row);
    }

    public ConsoleTable appendColumn(Object value) {
        if (value == null) {
            value = "(null)";
        }
        List<Object> row = rows.get(rows.size() - 1);
        row.add(value);
        int len = value.toString().getBytes().length;
        if (columnLength[row.size() - 1] < len) {
            columnLength[row.size() - 1] = len;
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < column; i++) {
            if (i == 0) {
                result.append('┌');
            } else {
                result.append('┬');
            }
            result.append(repeatChar('─', margin * 2 + columnLength[i]));
        }
        result.append("┐\n");

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            List row = rows.get(rowIndex);
            for (int i = 0; i < column; i++) {
                String item = "";
                if (i < row.size()) {
                    item = row.get(i).toString();
                }
                result.append('│').append(repeatChar(' ', margin)).append(item);
                result.append(repeatChar(' ', columnLength[i] - item.getBytes().length + margin));
            }
            result.append("│\n");
            if (rowIndex == rows.size() - 1) {
                for (int i = 0; i < column; i++) {
                    if (i == 0) {
                        result.append('└');
                    } else {
                        result.append('┴');
                    }
                    result.append(repeatChar('─', margin * 2 + columnLength[i]));
                }
                result.append("┘\n");
            } else {
                for (int i = 0; i < column; i++) {
                    if (i == 0) {
                        if (rowIndex == 0) {
                            result.append('├');//├┼┤
                        } else {
                            result.append('│');
                        }
                    } else {
                        if (rowIndex == 0) {
                            result.append('┼');//├┼┤
                        } else {
                            result.append('│');
                        }
                    }
                    if (rowIndex == 0) {
                        result.append(repeatChar('─', margin * 2 + columnLength[i]));
                    } else {
                        result.append(repeatChar(' ', margin * 2 + columnLength[i]));
                    }
                }
                if (rowIndex == 0) {
                    result.append("┤\n");
                } else {
                    result.append("│\n");
                }
            }
        }
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
