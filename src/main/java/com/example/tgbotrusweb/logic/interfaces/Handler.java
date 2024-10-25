package com.example.tgbotrusweb.logic.interfaces;

import com.example.tgbotrusweb.logic.domain.Comment;

public abstract class Handler {
  protected Handler next;

  public void setNext(Handler next) {
    this.next = next;
  }

  public abstract void handleUpdate(Comment comment);
}
