package representation.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Utils {
    private static final String[] VEHICLE_TYPES = new String[]{"Car", "Motorbike", "Skate", "Bike"};
    private static final String[] OPERATION_TYPES = new String[]{"Maintenance", "Repair"};

    public static String[] getVehicleTypes() {
        return VEHICLE_TYPES;
    }

    public static String[] getOperationTypes() {
        return OPERATION_TYPES;
    }

    public static String generateErrorMessage(List<String> uncompletedFields) {
        StringBuilder errorMessages = new StringBuilder();
        int i = 1;
        for (String s : uncompletedFields) {
            errorMessages.append(s);
            if (i == uncompletedFields.size() - 1)
                errorMessages.append(", and ");
            else if (i != uncompletedFields.size())
                errorMessages.append(", ");
            else
                errorMessages.append(" ");
            i++;
        }
        errorMessages.append("can not be empty!");
        return String.valueOf(errorMessages);
    }

    public static String[][] convertResultSetToArray(ResultSet resultSet, Map<String, String> columnsList) {
        List<List<String>> array = new ArrayList<>();
        try {
            array.add(new ArrayList<>(columnsList.keySet()));
            while (resultSet.next()) {
                List<String> values = new ArrayList<>();
                for (String s : columnsList.keySet()) {
                    values.add(getResultSetValue(resultSet, s, columnsList.get(s)));
                }
                array.add(values);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return array.stream()
                .map(l -> l.toArray(String[]::new))
                .toArray(String[][]::new);
    }

    public static String[][] mergeResultSetArrays(List<String[][]> arrays) {
        Set<String> attributes = new HashSet<>();
        for (String[][] ar : arrays) {
            for (String column : ar[0]) {
                attributes.add(column);
            }
        }
        List<List<String>> mergedList = new ArrayList<>();
        mergedList.add(new ArrayList<>(attributes));
        for (String[][] ar : arrays) {
            List<String> array = new ArrayList<>();
            for (String[] subar : ar) {
                int i = 0;
                array.clear();
                for (String col : subar) {
                    if (Arrays.asList(ar[0]).contains(ar[0][i++]))
                        array.add(col);
                    else
                        array.add(null);
                }
                mergedList.add(array);
            }
        }
        return mergedList.stream()
                .map(l -> l.toArray(String[]::new))
                .toArray(String[][]::new);
    }

    private static String getResultSetValue(ResultSet resultSet, String column, String type) throws SQLException {
        if (type == "int") {
            return String.valueOf(resultSet.getInt(column));
        } else if (type == "float") {
            return String.valueOf(resultSet.getFloat(column));
        } else {
            return String.valueOf(resultSet.getString(column));
        }
    }

    public static boolean isDouble(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isInt(String str) {
        return str.matches("[0-9]+");
    }
}
