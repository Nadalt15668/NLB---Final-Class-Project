package nadav.altabet.nlb;

public class User {

    private String email;
    private String first_name;
    private String last_name;
    private String ID;
    private String city;
    private String address;
    private String phone;
    private String pic;
    private nadav.altabet.nlb.Date date_of_birth;
    private String gender;
    private String type;//Parent / Coordinator / Admin
    private String branch_name;

    public User(String email, String first_name, String last_name, String ID, String city, String address,
                String phone, String pic, nadav.altabet.nlb.Date date_of_birth, String gender, String type, String branch_name) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ID = ID;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.pic = pic;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.type = type;
        this.branch_name = branch_name;
    }

    public User() {
    }

    //-----------------------------------------------------------------
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    //-----------------------------------------------------------------
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    //-----------------------------------------------------------------
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    //-----------------------------------------------------------------
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    //-----------------------------------------------------------------
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    //-----------------------------------------------------------------
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    //-----------------------------------------------------------------
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    //-----------------------------------------------------------------
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    //-----------------------------------------------------------------
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    //-----------------------------------------------------------------
    public nadav.altabet.nlb.Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    //-----------------------------------------------------------------
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    //-----------------------------------------------------------------
    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
    //-----------------------------------------------------------------

}
