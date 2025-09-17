package com.example.petfriend.common.contants;

public class ApiMappingPattern {

    public static final String API = "/api";
    public static final String V1 = "/v1";
    public static final String BASE = API + V1;


    public static final class Tasks {
        private Tasks() {}

        public static final String ROOT = BASE + "tasks";
        public static final String ID_ONLY = "/{taskId}";
    }
}