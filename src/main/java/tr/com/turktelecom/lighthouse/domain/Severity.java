package tr.com.turktelecom.lighthouse.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by 010235 on 17.03.2016.
 */
public enum Severity{
    INFO("0","INFO"),LOW("1", "LOW"), MEDIUM("2", "MEDIUM"), HIGH("3", "HIGH"), CRITICAL("4", "CRITICAL");

    private String no;
    private String title;

    Severity(String no, String title){
        this.no = no;
        this.title = title;
    }

    public static Severity fromNo(String no){
        switch (no){
            case "0":
                return INFO;
            case "1":
                return LOW;
            case "2":
                return MEDIUM;
            case "3":
                return HIGH;
            case "4":
                return CRITICAL;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "Severity{" +
            "no='" + no + '\'' +
            "title='" + title + '\'' +
            '}';
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

    public String getNo() {
        return no;
    }
}
