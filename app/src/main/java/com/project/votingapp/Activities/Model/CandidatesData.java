package com.project.votingapp.Activities.Model;

public class CandidatesData {

    private String imgID;
    private String candidates_ID, candidates_name, candidates_partyList, candidates_yearLevel, candidates_section, candidates_positionRunningFor;;
    private Integer candidates_votes;

    public CandidatesData(){
    }

    public CandidatesData(String url, String candidates_ID, String candidates_name, String candidates_partyList, String candidates_yearLevel, String candidates_section, String candidates_positionRunningFor, int candidates_votes) {
        this.imgID = url;
        this.candidates_ID = candidates_ID;
        this.candidates_name = candidates_name;
        this.candidates_partyList = candidates_partyList;
        this.candidates_yearLevel = candidates_yearLevel;
        this.candidates_section = candidates_section;
        this.candidates_positionRunningFor = candidates_positionRunningFor;
        this.candidates_votes = candidates_votes;
    }

    public Integer getCandidatesVotes() {
        return candidates_votes;
    }

    public void setCandidatesVotes(Integer candidates_votes) {
        this.candidates_votes = candidates_votes;
    }

    public String getCandidatesID() {
        return candidates_ID;
    }

    public void setCandidatesID(String candidates_ID) {
        this.candidates_ID = candidates_ID;
    }

    public String getCandidatesName() {
        return candidates_name;
    }

    public void setCandidatesName(String candidates_name) {
        this.candidates_name = candidates_name;
    }

    public String getCandidatesPartyList() {
        return candidates_partyList;
    }

    public void setCandidatesPartyList(String candidates_partyList) {
        this.candidates_partyList = candidates_partyList;
    }

    public String getCandidatesYearLevel() {
        return candidates_yearLevel;
    }

    public void setCandidatesYearLevel(String candidates_yearLevel) {
        this.candidates_yearLevel = candidates_yearLevel;
    }

    public String getCandidatesSection() {
        return candidates_section;
    }

    public void setCandidatesSection(String candidates_section) {
        this.candidates_section = candidates_section;
    }

    public String getCandidatesPositionRunningFor() {
        return candidates_positionRunningFor;
    }

    public void setCandidatesPositionRunningFor(String candidates_positionRunningFor) {
        this.candidates_positionRunningFor = candidates_positionRunningFor;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
