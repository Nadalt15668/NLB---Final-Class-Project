package nadav.altabet.nlb;

import java.util.Calendar;

public class Worker {

    private String email;
    private Date startDate;
    private Date endDate;
    private String Role;

    public Worker(String email, Date startDate, String Role) {
        this.email = email;
        this.startDate = startDate;
        this.endDate = new Date();
        this.Role = Role;
    }

    public Worker() {
    }

    public Worker(Worker w)
    {
        this.email = w.getEmail();
        this.startDate = w.getStartDate();
        this.endDate.setDay(Calendar.getInstance().DAY_OF_MONTH);
        this.endDate.setMonth(Calendar.getInstance().MONTH);
        this.endDate.setYear(Calendar.getInstance().YEAR);
        this.endDate.setMinute(Calendar.getInstance().MINUTE);
        this.endDate.setHour(Calendar.getInstance().HOUR_OF_DAY);
        this.Role = w.getRole();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
