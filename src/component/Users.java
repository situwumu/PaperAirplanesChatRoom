package component;

public class Users {
    private String username;
    private String password;
    private String gender;
    private String telephone;
    //登录状态，2 离线 ，1 在线， 0 隐身
    private int status;

    public Users() {
    }
   public Users(String username,String password){
        this.username = username;
        this.password = password;
   }
    public Users(String username, String password,String telephone, String gender,int status) {
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.gender = gender;
        this.status = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
