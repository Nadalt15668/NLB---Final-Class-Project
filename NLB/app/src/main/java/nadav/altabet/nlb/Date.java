package nadav.altabet.nlb;

public class Date {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public Date(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Date() {
    }
//------------------------------------------------------
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }
    //-------------------------------------------------
    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
    //--------------------------------------------------
    public int checkForAbove18(Date currentDate)
    {
        if (currentDate.getMonth()-this.month>0) {
            return currentDate.getYear()-this.year;
        }
        if (currentDate.getMonth()==month) {
            if (currentDate.getDay() - this.day >= 0) {
                return currentDate.getYear()-this.year;
            }
            return currentDate.getYear()-this.year-1;
        }
        return currentDate.getYear()-this.year-1;
    }
    public boolean isPast(Date date)
    {
        if (this.year - date.getYear() < 0)
            return true;
        if (this.year - date.getYear() == 0)
        {
            if (this.month - date.getMonth() <0)
                return true;
            if (this.getMonth() - date.getMonth() == 0)
            {
                if (this.day - date.getDay() < 0)
                    return  true;
                if (this.day - date.getDay() == 0)
                    return true;
                return false;
            }
        }
        return false;
    }

}
