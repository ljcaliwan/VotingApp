package com.project.votingapp.Activities.Model;

public class ElectionData {

    private String adminEmail, election_code, election_name, election_type;
    private Integer stud_ID_or_LRN ;

    public ElectionData(){ }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Integer getStud_ID_or_LRN() {
        return stud_ID_or_LRN;
    }

    public void setStud_ID_or_LRN(Integer stud_ID_or_LRN) {
        this.stud_ID_or_LRN = stud_ID_or_LRN;
    }

    public String getElectionCode() { return election_code; }

    public void setElectionCode(String election_code) { this.election_code = election_code; }

    public String getElectionName() { return election_name; }

    public void setElectionName(String election_name) { this.election_name = election_name; }

    public String getElectionType() {
        return election_type;
    }

    public void setElectionType(String election_type) {
        this.election_type = election_type;
    }


}
