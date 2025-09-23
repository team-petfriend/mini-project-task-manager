package com.example.petfriend.common.contants;

public class ApiMappingPattern {

    public static final String API = "/api";
    public static final String V1 = "/v1";
    public static final String BASE = API + V1;


    public static final class Tasks {
        private Tasks() {}

        public static final String ROOT = BASE + "/tasks";
        public static final String ID_ONLY = "/{taskId}";
        public static final String By_ID = ROOT + ID_ONLY;
        public static final String STATUS = ID_ONLY +"/statusUpdate";
        public static final String PRIORITY = ID_ONLY + "/priority";
    }

    public static final class User {
        private User() {}
        public static final String AUTH = BASE + "/auth";

        public static final String ADMIN = BASE + "/admin";
        public static final String ADD_ROLES = ADMIN + "/roles/add";
        public static final String REPLACE_ROLES = ADMIN + "/roles/replace";
        public static final String REMOVE_ROLES = ADMIN + "/roles/remove";
    }

    public static final class Project{
        private Project(){}

        public static final String ROOT = BASE + "/projects";
        public static final String SEARCH = "/search";
        public static final String ONLY_ID ="/{projectId}";
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
        public static final String ID_ONLY = "/{notificationId}";
        public static final String BY_ID = ROOT + ID_ONLY + "/read";
    }
}