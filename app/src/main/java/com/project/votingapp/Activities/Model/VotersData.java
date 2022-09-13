package com.project.votingapp.Activities.Model;

public class VotersData {

    private String voterName, voterLRN_Or_ID, voterYearOrGradeLvl, voterSection;
    private String adminEmail;
    public VotersData(){

    }

    public VotersData(String voterName, String voterLRN_Or_ID, String voterYearOrGradeLvl, String voterSection) {
        this.voterName = voterName;
        this.voterLRN_Or_ID = voterLRN_Or_ID;
        this.voterYearOrGradeLvl = voterYearOrGradeLvl;
        this.voterSection = voterSection;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getVoterID() {
        return voterLRN_Or_ID;
    }

    public void setVoterID(String voterLRN_Or_ID) {
        this.voterLRN_Or_ID = voterLRN_Or_ID;
    }

    public String getVoterYearLevel() {
        return voterYearOrGradeLvl;
    }

    public void setVoterYearLevel(String voterYearOrGradeLvl) {
        this.voterYearOrGradeLvl = voterYearOrGradeLvl;
    }
    public String getVoterSection() {
        return voterSection;
    }

    public void setVoterSection(String voterSection) {
        this.voterSection = voterSection;
    }
}
