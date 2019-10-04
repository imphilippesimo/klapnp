package com.klapnp.core.notification;

public class NotificationContext<E> {

    private String to;

    private E entity;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }
}
