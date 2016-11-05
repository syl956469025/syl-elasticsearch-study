package syl.study.elasticsearch.model;

/**
 * Created by Mtime on 2016/10/19.
 */
public class Points extends BaseEntity<Long> {

    int level;

    int point;

    long userId;

    String[] cards;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }
}
