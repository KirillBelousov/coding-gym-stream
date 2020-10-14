package codinggym.stream;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class Post {
    private String title;
    private List<String> authors;
    private PostType type; // BLOG, INTERVIEW, PODCAST
    private LocalDate date;
    private URL url;
    private int likes;

    public Post(String title, List<String> authors, PostType type, LocalDate date, String urlString, int likes) {
        this.title = title;
        this.authors = authors;
        this.type = type;
        this.date = date;
        try {
            this.url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public PostType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public URL getUrl() {
        return url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", type=" + type +
                ", date=" + date +
                ", url=" + url +
                ", likes=" + likes +
                '}';
    }
}
