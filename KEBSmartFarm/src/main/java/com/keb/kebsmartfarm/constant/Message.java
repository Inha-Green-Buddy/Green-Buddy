package com.keb.kebsmartfarm.constant;

public class Message {
    public static class Error {
        public static final String ALREADY_REGISTERD_KIT = "이미 등록된 키트입니다.";
        public static final String KIT_NOT_EXIST = "해당 키트가 존재하지 않습니다.";
        public static final String KIT_NOT_REGISTERED = "등록되지 않은 키트입니다.";
        public static final String ALREADY_EXIST_USER = "이미 가입되어 있는 유저입니다";
        public static final String USER_DOES_NOT_MACTH = "%s에 해당하는 회원이 없습니다";

        public static final String INVALID_SERIAL_NUMBER = "존재하지 않는 시리얼 번호입니다.";
        public static final String FAILED_TO_SAVE_FILE = "파일 저장에 실패했습니다.";
        public static final String CAN_NOT_READ_FILE = "파일을 읽을 수 없습니다 : %s";
        public static final String KIT_ALREADY_HAVE_PLANT = "이미 식물이 등록된 키트입니다.";
        public static final String PLANT_NOT_REGISTERED = "식물이 등록되지 않았습니다.";
        public static final String PLANT_NOT_EXIST = "식물이 존재하지 않습니다.";
        public static final String NO_ACTIVE_PLANT = "현재 키우는 식물이 없습니다.";

        public static final String NO_LOGIN_USER_INFORMATION = "로그인 유저 정보가 없습니다";
        public static final String PASSWORD_DOES_NOT_MATCH = "비밀번호가 맞지 않습니다";
        public static final String ID_OR_PASSWORD_DOES_NOT_MATCH = "아이디 또는 패스워드가 일치하지 않습니다.";

        public static final String CAN_NOT_STORE_EMPTY_FILE = "빈 파일은 저장할 수 없습니다.";
        public static final String NOT_IMAGE_FILE = "이미지 파일이 아닙니다. ";
        public static final String CAN_NOT_STORE_OUTSIDE_OF_DIRECTORY = "파일은 현재 디렉토리 바깥에 저장될 수 없습니다.";

        public static final String NO_CERTIFICATE_INFORMATION = "Security Context에 인증 정보가 없습니다";
    }

    public static final String SENT_EMAIL_TO_USER = "해당 이메일로 메일을 보냈습니다.";
}