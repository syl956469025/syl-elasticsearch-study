package syl.study.elasticsearch.model;

/**
 * Created by Mtime on 2016/11/3.
 */
public class Kequn extends BaseEntity<String> {


    private String userId;

    private String kid;

    private String cinemaId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }
}
