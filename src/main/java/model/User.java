package model;

import java.time.LocalDate;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String gender;
    private String phoneNumber;
    private String address;
    private String profileImage;

    // Private constructor to prevent direct instantiation
    private User(Builder builder) {
        this.userId = builder.userId;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.password = builder.password;
        this.birthDate = builder.birthDate;
        this.gender = builder.gender;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
        this.profileImage = builder.profileImage;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public static class Builder {
        private int userId;
        private String fullName;
        private String email;
        private String password;
        private LocalDate birthDate;
        private String gender;
        private String phoneNumber;
        private String address;
        private String profileImage;

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setProfileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        // Build method to create a User object
        public User build() {
            return new User(this);
        }
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}