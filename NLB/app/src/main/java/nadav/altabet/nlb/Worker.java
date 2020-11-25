package nadav.altabet.nlb;

public class Worker {

    private String email;
    private Date startDate;
    private Date endDate;
    private String Role;

    public Worker(String email, Date startDate, Date endDate, String Role) {
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
        this.Role = Role;
    }

    public Worker(String email, Date startDate, String Role) {
        this.email = email;
        this.startDate = startDate;
        this.endDate = new Date();
        this.Role = Role;
    }

    public Worker() {
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
}
