package com.jupiter.web.manager.bus.admin.dto;

import lombok.Data;

public class PermissionTree {

    //角色id
    private Long roleId;

    private Long id;
    private String parent;
    private String text;
    private String icon;
    private State state;
    private Long sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public void setOpened(boolean opened) {
        if (state == null) {
            state = new State();
            state.setOpened(opened);
        } else {
            state.setOpened(opened);
        }
    }

    public void setSelected(boolean selected) {
        if (state == null) {
            state = new State();
            state.setSelected(selected);
        } else {
            state.setSelected(selected);
        }
    }

    @Data
    class State {

        private boolean opened = false;

        private boolean selected = false;

        private boolean disabled = false;
    }
}
