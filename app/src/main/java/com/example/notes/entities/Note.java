package com.example.notes.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note implements Comparable<Note> {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String title;

    @ColumnInfo
    private String dateTime;

    @ColumnInfo
    private String subtitle;

    @ColumnInfo
    private String noteText;

    @ColumnInfo
    private String imagePath;

    @ColumnInfo
    private String color;

    @ColumnInfo
    private String webLink;

    private boolean isChecked = false;

    public Note(String title, String dateTime, String subtitle, String noteText, String imagePath, String color, String webLink) {
        this.title = title;
        this.dateTime = dateTime;
        this.subtitle = subtitle;
        this.noteText = noteText;
        this.imagePath = imagePath;
        this.color = color;
        this.webLink = webLink;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Nullable
    @Override
    public String toString() {
        return title + " : " + dateTime;
    }

    @Override
    public int compareTo(Note o) {
        return this.id - o.getId();
    }
}
