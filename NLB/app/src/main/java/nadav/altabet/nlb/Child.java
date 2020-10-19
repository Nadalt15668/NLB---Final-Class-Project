package nadav.altabet.nlb;

public class Child {

    private String child_first_name;
    private String child_last_name;
    private Date child_birthdate;
    private String parent_UID;
    private String child_ID;
    private String child_gender;
    private String child_class_letter;
    private Integer child_class_number;
    private String child_school;
    private String child_branch;
    private String child_phone;//לא חובה
    private String child_email;//לא חובה
    private String child_profile;//לא חובה

    public Child(String child_first_name, String child_last_name, String parent_UID, Date child_birthdate, String child_ID, String child_gender,
                 String child_class_letter, Integer child_class_number, String child_school, String child_branch, String child_phone, String child_email, String child_profile)
    {
        this.child_first_name = child_first_name;
        this.child_last_name = child_last_name;
        this.parent_UID = parent_UID;
        this.child_birthdate = child_birthdate;
        this.child_ID = child_ID;
        this.child_gender = child_gender;
        this.child_class_letter = child_class_letter;
        this.child_class_number = child_class_number;
        this.child_school = child_school;
        this.child_branch = child_branch;
        if(child_phone.equals(null))
            this.child_phone = "";
        else
            this.child_phone = child_phone;
        if (child_email.equals(null))
            this.child_email = "";
        else
            this.child_email = child_email;
        if (child_profile.equals(null))
            this.child_profile = "";
        else
            this.child_profile = child_profile;
    }

    public Child() {
    }
    //-----------------------------------------------------
    public String getChild_school() {
        return child_school;
    }

    public void setChild_school(String child_school) {
        this.child_school = child_school;
    }
    //-----------------------------------------------------
    public String getChild_first_name() {
        return child_first_name;
    }

    public void setChild_first_name(String child_name) {
        this.child_first_name = child_name;
    }
    //-----------------------------------------------------
    public String getChild_last_name() {
        return child_last_name;
    }

    public void setChild_last_name(String child_last_name) {
        this.child_last_name = child_last_name;
    }
    // -----------------------------------------------------
    public String getParent_UID() {
        return parent_UID;
    }

    public void setParent_UID(String parent_ID) {
        this.parent_UID = parent_ID;
    }
    // -----------------------------------------------------
    public Date getChild_birthdate() {
        return child_birthdate;
    }

    public void setChild_birthdate(Date child_birthdate) {
        this.child_birthdate = child_birthdate;
    }
    //-----------------------------------------------------
    public String getChild_ID() {
        return child_ID;
    }

    public void setChild_ID(String child_ID) {
        this.child_ID = child_ID;
    }
    //-----------------------------------------------------
    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }
    //-----------------------------------------------------
    public String getChild_class_letter() {
        return child_class_letter;
    }

    public void setChild_class_letter(String child_class_letter) {
        this.child_class_letter = child_class_letter;
    }
    //-----------------------------------------------------
    public Integer getChild_class_number() {
        return child_class_number;
    }

    public void setChild_class_number(Integer child_class_number) {
        this.child_class_number = child_class_number;
    }
    //-----------------------------------------------------
    public String getChild_branch() {
        return child_branch;
    }

    public void setChild_branch(String child_branch) {
        this.child_branch = child_branch;
    }
    //-----------------------------------------------------
    public String getChild_phone() {
        return child_phone;
    }

    public void setChild_phone(String child_phone) {
        this.child_phone = child_phone;
    }
    //-----------------------------------------------------
    public String getChild_email() {
        return child_email;
    }

    public void setChild_email(String child_email) {
        this.child_email = child_email;
    }
    //-----------------------------------------------------
    public String getChild_profile() {
        return child_profile;
    }

    public void setChild_profile(String child_profile) {
        this.child_profile = child_profile;
    }
    //-----------------------------------------------------
}
