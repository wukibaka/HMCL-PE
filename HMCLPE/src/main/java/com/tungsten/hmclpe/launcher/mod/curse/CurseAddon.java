package com.tungsten.hmclpe.launcher.mod.curse;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tungsten.hmclpe.launcher.mod.ModListBean;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurseAddon implements ModListBean.IMod {
    private final int id;
    private final String name;
    private final List<Author> authors;
    private final List<Attachment> attachments;
    private final String websiteUrl;
    private final int gameId;
    private final String summary;
    private final int defaultFileId;
    private final List<LatestFile> latestFiles;
    private final List<Category> categories;
    private final int status;
    private final int primaryCategoryId;
    private final String slug;
    private final List<GameVersionLatestFile> gameVersionLatestFiles;
    private final boolean isFeatured;
    private final double popularityScore;
    private final int gamePopularityRank;
    private final String primaryLanguage; // e.g. enUS
    private final List<String> modLoaders;
    private final boolean isAvailable;
    private final boolean isExperimental;

    public CurseAddon(int id, String name, List<Author> authors, List<Attachment> attachments, String websiteUrl, int gameId, String summary, int defaultFileId, List<LatestFile> latestFiles, List<Category> categories, int status, int primaryCategoryId, String slug, List<GameVersionLatestFile> gameVersionLatestFiles, boolean isFeatured, double popularityScore, int gamePopularityRank, String primaryLanguage, List<String> modLoaders, boolean isAvailable, boolean isExperimental) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.attachments = attachments;
        this.websiteUrl = websiteUrl;
        this.gameId = gameId;
        this.summary = summary;
        this.defaultFileId = defaultFileId;
        this.latestFiles = latestFiles;
        this.categories = categories;
        this.status = status;
        this.primaryCategoryId = primaryCategoryId;
        this.slug = slug;
        this.gameVersionLatestFiles = gameVersionLatestFiles;
        this.isFeatured = isFeatured;
        this.popularityScore = popularityScore;
        this.gamePopularityRank = gamePopularityRank;
        this.primaryLanguage = primaryLanguage;
        this.modLoaders = modLoaders;
        this.isAvailable = isAvailable;
        this.isExperimental = isExperimental;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public int getGameId() {
        return gameId;
    }

    public String getSummary() {
        return summary;
    }

    public int getDefaultFileId() {
        return defaultFileId;
    }

    public List<LatestFile> getLatestFiles() {
        return latestFiles;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getStatus() {
        return status;
    }

    public int getPrimaryCategoryId() {
        return primaryCategoryId;
    }

    public String getSlug() {
        return slug;
    }

    public List<GameVersionLatestFile> getGameVersionLatestFiles() {
        return gameVersionLatestFiles;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public double getPopularityScore() {
        return popularityScore;
    }

    public int getGamePopularityRank() {
        return gamePopularityRank;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public List<String> getModLoaders() {
        return modLoaders;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isExperimental() {
        return isExperimental;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<ModListBean.Mod> loadDependencies() throws IOException {
        Set<Integer> dependencies = latestFiles.stream()
                .flatMap(latestFile -> latestFile.getDependencies().stream())
                .filter(dep -> dep.getType() == 3)
                .map(Dependency::getAddonId)
                .collect(Collectors.toSet());
        List<ModListBean.Mod> mods = new ArrayList<>();
        for (int dependencyId : dependencies) {
            mods.add(CurseModManager.getAddon(dependencyId).toMod());
        }
        return mods;
    }

    @Override
    public Stream<ModListBean.Version> loadVersions() throws IOException {
        return CurseModManager.getFiles(this).stream()
                .map(CurseAddon.LatestFile::toVersion);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ModListBean.Mod toMod() {
        String iconUrl = null;
        for (CurseAddon.Attachment attachment : attachments) {
            if (attachment.isDefault()) {
                iconUrl = attachment.getThumbnailUrl();
            }
        }

        return new ModListBean.Mod(
                slug,
                "",
                name,
                summary,
                categories.stream().map(category -> Integer.toString(category.getCategoryId())).collect(Collectors.toList()),
                websiteUrl,
                iconUrl,
                this
        );
    }

    public static class Author {
        private final String name;
        private final String url;
        private final int projectId;
        private final int id;
        private final int userId;
        private final int twitchId;

        public Author(String name, String url, int projectId, int id, int userId, int twitchId) {
            this.name = name;
            this.url = url;
            this.projectId = projectId;
            this.id = id;
            this.userId = userId;
            this.twitchId = twitchId;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public int getProjectId() {
            return projectId;
        }

        public int getId() {
            return id;
        }

        public int getUserId() {
            return userId;
        }

        public int getTwitchId() {
            return twitchId;
        }
    }

    public static class Attachment {
        private final int id;
        private final int projectId;
        private final String description;
        private final boolean isDefault;
        private final String thumbnailUrl;
        private final String title;
        private final String url;
        private final int status;

        public Attachment(int id, int projectId, String description, boolean isDefault, String thumbnailUrl, String title, String url, int status) {
            this.id = id;
            this.projectId = projectId;
            this.description = description;
            this.isDefault = isDefault;
            this.thumbnailUrl = thumbnailUrl;
            this.title = title;
            this.url = url;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public int getProjectId() {
            return projectId;
        }

        public String getDescription() {
            return description;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class Dependency {
        private final int id;
        private final int addonId;
        private final int type;
        private final int fileId;

        public Dependency() {
            this(0, 0, 0, 0);
        }

        public Dependency(int id, int addonId, int type, int fileId) {
            this.id = id;
            this.addonId = addonId;
            this.type = type;
            this.fileId = fileId;
        }

        public int getId() {
            return id;
        }

        public int getAddonId() {
            return addonId;
        }

        public int getType() {
            return type;
        }

        public int getFileId() {
            return fileId;
        }
    }

    public static class LatestFile {
        private final int id;
        private final String displayName;
        private final String fileName;
        private final String fileDate;
        private final int fileLength;
        private final int releaseType;
        private final int fileStatus;
        private final String downloadUrl;
        private final boolean isAlternate;
        private final int alternateFileId;
        private final List<Dependency> dependencies;
        private final boolean isAvailable;
        private final List<String> gameVersion;
        private final boolean hasInstallScript;
        private final boolean isCompatibleWIthClient;
        private final int categorySectionPackageType;
        private final int restrictProjectFileAccess;
        private final int projectStatus;
        private final int projectId;
        private final boolean isServerPack;
        private final int serverPackFileId;

        private transient Instant fileDataInstant;

        public LatestFile(int id, String displayName, String fileName, String fileDate, int fileLength, int releaseType, int fileStatus, String downloadUrl, boolean isAlternate, int alternateFileId, List<Dependency> dependencies, boolean isAvailable, List<String> gameVersion, boolean hasInstallScript, boolean isCompatibleWIthClient, int categorySectionPackageType, int restrictProjectFileAccess, int projectStatus, int projectId, boolean isServerPack, int serverPackFileId) {
            this.id = id;
            this.displayName = displayName;
            this.fileName = fileName;
            this.fileDate = fileDate;
            this.fileLength = fileLength;
            this.releaseType = releaseType;
            this.fileStatus = fileStatus;
            this.downloadUrl = downloadUrl;
            this.isAlternate = isAlternate;
            this.alternateFileId = alternateFileId;
            this.dependencies = dependencies;
            this.isAvailable = isAvailable;
            this.gameVersion = gameVersion;
            this.hasInstallScript = hasInstallScript;
            this.isCompatibleWIthClient = isCompatibleWIthClient;
            this.categorySectionPackageType = categorySectionPackageType;
            this.restrictProjectFileAccess = restrictProjectFileAccess;
            this.projectStatus = projectStatus;
            this.projectId = projectId;
            this.isServerPack = isServerPack;
            this.serverPackFileId = serverPackFileId;
        }

        public int getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileDate() {
            return fileDate;
        }

        public int getFileLength() {
            return fileLength;
        }

        public int getReleaseType() {
            return releaseType;
        }

        public int getFileStatus() {
            return fileStatus;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public boolean isAlternate() {
            return isAlternate;
        }

        public int getAlternateFileId() {
            return alternateFileId;
        }

        public List<Dependency> getDependencies() {
            return dependencies;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public List<String> getGameVersion() {
            return gameVersion;
        }

        public boolean isHasInstallScript() {
            return hasInstallScript;
        }

        public boolean isCompatibleWIthClient() {
            return isCompatibleWIthClient;
        }

        public int getCategorySectionPackageType() {
            return categorySectionPackageType;
        }

        public int getRestrictProjectFileAccess() {
            return restrictProjectFileAccess;
        }

        public int getProjectStatus() {
            return projectStatus;
        }

        public int getProjectId() {
            return projectId;
        }

        public boolean isServerPack() {
            return isServerPack;
        }

        public int getServerPackFileId() {
            return serverPackFileId;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Instant getParsedFileDate() {
            if (fileDataInstant == null) {
                fileDataInstant = Instant.parse(fileDate);
            }
            return fileDataInstant;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ModListBean.Version toVersion() {
            ModListBean.VersionType versionType;
            switch (getReleaseType()) {
                case 1:
                    versionType = ModListBean.VersionType.Release;
                    break;
                case 2:
                    versionType = ModListBean.VersionType.Beta;
                    break;
                case 3:
                    versionType = ModListBean.VersionType.Alpha;
                    break;
                default:
                    versionType = ModListBean.VersionType.Release;
                    break;
            }

            return new ModListBean.Version(
                    this,
                    getDisplayName(),
                    null,
                    null,
                    getParsedFileDate(),
                    versionType,
                    new ModListBean.File(Collections.emptyMap(), getDownloadUrl(), getFileName()),
                    Collections.emptyList(),
                    gameVersion.stream().filter(ver -> ver.startsWith("1.") || ver.contains("w")).collect(Collectors.toList()),
                    Collections.emptyList()
            );
        }
    }

    public static class Category {
        private final int categoryId;
        private final String name;
        private final String url;
        private final String avatarUrl;
        private final int parentId;
        private final int rootId;
        private final int projectId;
        private final int avatarId;
        private final int gameId;

        public Category(int categoryId, String name, String url, String avatarUrl, int parentId, int rootId, int projectId, int avatarId, int gameId) {
            this.categoryId = categoryId;
            this.name = name;
            this.url = url;
            this.avatarUrl = avatarUrl;
            this.parentId = parentId;
            this.rootId = rootId;
            this.projectId = projectId;
            this.avatarId = avatarId;
            this.gameId = gameId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public int getParentId() {
            return parentId;
        }

        public int getRootId() {
            return rootId;
        }

        public int getProjectId() {
            return projectId;
        }

        public int getAvatarId() {
            return avatarId;
        }

        public int getGameId() {
            return gameId;
        }
    }

    public static class GameVersionLatestFile {
        private final String gameVersion;
        private final String projectFileId;
        private final String projectFileName;
        private final int fileType;
        private final Integer modLoader; // optional

        public GameVersionLatestFile(String gameVersion, String projectFileId, String projectFileName, int fileType, Integer modLoader) {
            this.gameVersion = gameVersion;
            this.projectFileId = projectFileId;
            this.projectFileName = projectFileName;
            this.fileType = fileType;
            this.modLoader = modLoader;
        }

        public String getGameVersion() {
            return gameVersion;
        }

        public String getProjectFileId() {
            return projectFileId;
        }

        public String getProjectFileName() {
            return projectFileName;
        }

        public int getFileType() {
            return fileType;
        }

        public Integer getModLoader() {
            return modLoader;
        }
    }
}
