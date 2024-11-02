package com.mcgill.ecse321.GameShop.dto;




public class PlatformRequestDto {
   
    private String platformName;
  
    private String managerEmail;

    public PlatformRequestDto() {
    }

    public PlatformRequestDto(String platformName, String managerEmail) {
        this.platformName = platformName;
        this.managerEmail = managerEmail;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }
}
