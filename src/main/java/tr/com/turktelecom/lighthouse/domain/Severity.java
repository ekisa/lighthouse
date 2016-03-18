package tr.com.turktelecom.lighthouse.domain;

/**
 * Created by 010235 on 17.03.2016.
 */
public enum Severity {

    HIGH("High"), MEDIUM("Medium"), LOW("Low");

    private String title;

    Severity(String title){
        this.title = title;
    }

    @Override
    public String toString() {
        return "Severity{" +
            "title='" + title + '\'' +
            '}';
    }
}
