package nadav.altabet.nlb;

public class Activity {

    private String activity_name;
    private Date activity_date;
    private Date creation_date;

    public Activity(String activity_name, Date activity_date, Date creation_date) {
        this.activity_name = activity_name;
        this.activity_date = activity_date;
        this.creation_date = creation_date;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public Date getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(Date activity_date) {
        this.activity_date = activity_date;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }
}
