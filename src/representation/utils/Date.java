package representation.utils;

public class Date {
    private int day;
    private int month;
    private int year;

    public Date(String day, String month, String year) {
        int dayInt = Integer.parseInt(day);
        int monthInt = Integer.parseInt(month);
        int yearInt = Integer.parseInt(year);
        verifyConstructorArguments(dayInt, monthInt);
        this.day = dayInt;
        this.month = monthInt;
        this.year = yearInt;
    }

    public String toString() {
        String day = this.day < 10 ? "0" + this.day : String.valueOf(this.day);
        String month = this.month < 10 ? "0" + this.month : String.valueOf(this.month);
        String year = String.valueOf(this.year);
        return day + "/" + month + "/" + year;
    }

    public static Date toDate(String date) {
        if(!isDateValid(date)) {
            return null;
        }

        String[] arr = date.split("/", 3);
        return new Date(arr[0], arr[1], arr[2]);
    }

    public static boolean isDateValid(String date) {
        String[] dateParts = date.split("/");
        return dateParts.length == 3 && isDayValid(dateParts[0]) && isMonthValid(dateParts[1]) && isYearValid(dateParts[2]);
    }

    public static boolean isDayValid(String day) {
        return day.length() == 2 && Utils.isInt(day) && Integer.parseInt(day) > 0 && Integer.parseInt(day) <= 31;
    }

    public static boolean isMonthValid(String month) {
        return month.length() == 2 && Utils.isInt(month) && Integer.parseInt(month) > 0 && Integer.parseInt(month) <= 12;
    }

    public static boolean isYearValid(String year) {
        return !year.isEmpty() && Utils.isInt(year);
    }

    private void verifyConstructorArguments(int day, int month) {
        assert (day > 0 && day <= 31 && month > 0 && month <= 12);
    }

    public static int getDaysDifference(String date1, String date2) {
        assert (isDateValid(date1) && isDateValid(date2));
        int daysDifference = 0;
        daysDifference += Integer.parseInt(String.valueOf(date2.charAt(0) + date2.charAt(1))) - Integer.parseInt(String.valueOf(date1.charAt(0) + date1.charAt(1)));
        daysDifference += (Integer.parseInt(String.valueOf(date2.charAt(3) + date2.charAt(4))) - Integer.parseInt(String.valueOf(date1.charAt(3) + date1.charAt(4)))) * 31;
        daysDifference += (Integer.parseInt(String.valueOf(date2.charAt(6) + date2.charAt(7) + date2.charAt(8) + date2.charAt(9))) - Integer.parseInt(String.valueOf(date1.charAt(6) + date1.charAt(7) + date1.charAt(8) + date1.charAt(9)))) * 365;
        return daysDifference;
    }

    public String extractDay(String date) {
        assert (isDateValid(date));
        return String.valueOf(date.charAt(0)) + date.charAt(1);
    }

    public String extractMonth(String date) {
        assert (isDateValid(date));
        return String.valueOf(date.charAt(3)) + date.charAt(4);
    }

    public String extractYear(String date) {
        assert (isDateValid(date));
        StringBuilder year = new StringBuilder();
        for (int i = 6; i<date.length(); i++) {
            year.append(date.charAt(i));
        }
        return year.toString();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
