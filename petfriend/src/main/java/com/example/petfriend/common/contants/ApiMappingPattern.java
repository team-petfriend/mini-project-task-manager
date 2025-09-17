package com.example.petfriend.common.contants;

public class ApiMappingPattern {

    public static final String API = "/api";
    public static final String V1 = "/v1";
    public static final String BASE = API + V1;


    public static final class Tasks {
        private Tasks() {}

        public static final String ROOT = BASE + "tasks";
        public static final String ID_ONLY = "/{taskId}";
        public static final String By_ID = ROOT + ID_ONLY;
    }

    public static final class Project{
        private Project(){}

        public static final String ROOT = BASE + "/projects";
        public static final String ONLY_NAME = ROOT + "/{projectName}";
        public static final String ONLY_ID = ROOT + "/{projectId}";
    }

    public static final class Comments {
        private Comments() {}

        public static final String ROOT = Tasks.By_ID + "/comments";
        public static final String ID_ONLY = "/{commentId}";
        public static final String SORT = "/sort";
    }

    public static final class Notifications {
        private Notifications() {}

        public static final String ROOT = BASE + "/notifications";
        public static final String ID_ONLY = "/notificationId";
        public static final String BY_ID = ROOT + ID_ONLY + "/read";
    }
}