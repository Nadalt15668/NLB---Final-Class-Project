package nadav.altabet.nlb;

public class Coordinator {

    private String email;
    private Date startDate;
    private Date endDate;

    public Coordinator(String email, Date startDate, Date endDate) {
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Coordinator(String email, Date startDate) {
        this.email = email;
        this.startDate = startDate;
        this.endDate = new Date();
    }

    public Coordinator() {
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
