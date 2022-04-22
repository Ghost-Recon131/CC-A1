package rmit.cc.a1.exceptions;

public class InvalidLoginResponse {


        private String error;

        public InvalidLoginResponse() {
            this.error = "Invalid Username or password";
        }


        public String getError() {
            return error;
        }
    }


