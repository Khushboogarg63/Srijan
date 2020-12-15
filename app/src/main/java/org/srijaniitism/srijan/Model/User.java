package org.srijaniitism.srijan.Model;

public class User {

    private String College;
    private String ProfilePic;


    public User(String college, String profilePic) {
        this.College = college;
        this.ProfilePic = profilePic;
    }

    public User() {
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }
}
