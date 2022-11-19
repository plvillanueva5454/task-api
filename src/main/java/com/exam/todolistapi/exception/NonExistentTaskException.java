package com.exam.todolistapi.exception;

public class NonExistentTaskException extends Exception{
    public NonExistentTaskException(String message) {
        super(message);
    }
}
