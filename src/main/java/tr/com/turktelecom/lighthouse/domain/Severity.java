package tr.com.turktelecom.lighthouse.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by 010235 on 17.03.2016.
 */
public enum Severity {

    CRITICAL("CRITICAL", "4"), HIGH("HIGH", "3"), MEDIUM("MEDIUM", "2"), LOW("LOW", "1"), INFO("INFO", "0");

    private String title;
    private String no;

    Severity(String title, String no){
        this.title = title;
        this.no = no;
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
                return INFO;
        }
    }

    @Override
    public String toString() {
        return "Severity{" +
            "title='" + title + '\'' +
            "no='" + no + '\'' +
            '}';
    }

    @JsonValue
    public String getTitle() {
        return title;
    }
}
