package nadav.altabet.nlb;

import java.util.ArrayList;

public class Activity {

    private String activity_name;
    private Date activity_date;
    private Date creation_date;
    private String  activity_class;
    private String file_url;
    private ArrayList<String> childUID;
    private String guideName;

    public Activity(String activity_name, Date activity_date, Date creation_date, String activity_class, String guideName) {
        this.activity_name = activity_name;
        this.activity_date = activity_date;
        this.creation_date = creation_date;
        this.activity_class = activity_class;
        this.file_url = "";
        this.childUID = new ArrayList<>();
        this.childUID.add("a string for non-null purposes");
        this.guideName = guideName;
    }

    public Activity() {
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

    public String getActivity_class() {
        return activity_class;
    }

    public void setActivity_class(String activity_class) {
        this.activity_class = activity_class;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public ArrayList<String> getChildUID() {
        return childUID;
    }

    public void setChildUID(ArrayList<String> childUID) {
        this.childUID = childUID;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }
}
