package okhttp.demo.com.okhttpdemo.retrofitDemo.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by uncle_charlie on 21/2/16.
 */
public class RepositoryModel implements Serializable {
    private int id;
    private String name;
    @SerializedName("full_name")
    private String fullName;

    private OwnerEntity owner;
    @SerializedName("private")
    private boolean privateX;
    private String description;
    private String url;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("git_url")
    private String gitUrl;
    private String homepage;
    private String language;
    @SerializedName("default_branch")
    private String defaultBranch;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setOwner(OwnerEntity owner) {
        this.owner = owner;
    }

    public void setPrivateX(boolean privateX) {
        this.privateX = privateX;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public OwnerEntity getOwner() {
        return owner;
    }

    public boolean isPrivateX() {
        return privateX;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getLanguage() {
        return language;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public static class OwnerEntity {
        private String login;
        private int id;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("html_url")
        private String htmlUrl;

        public void setLogin(String login) {
            this.login = login;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public String getLogin() {
            return login;
        }

        public int getId() {
            return id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }
    }
}
