package tests.utils.data;

public class ChallengeData {
    public String name;
    public String distance;
    public String description;
    public String terms;
    public String badge;
    public boolean isPrivate;

    // Constructor Utama (Untuk Positive Test / Data Lengkap)
    public ChallengeData(String name, String distance, String description, String terms, String badge, boolean isPrivate) {
        this.name = name;
        this.distance = distance;
        this.description = description;
        this.terms = terms;
        this.badge = badge;
        this.isPrivate = isPrivate;
    }

}