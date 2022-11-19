package com.exam.todolistapi.exception;

public class EmptyTaskListException extends Exception{
    public EmptyTaskListException(String message) {
        super(message);
    }
}
