package com.project.votingapp.Activities.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.votingapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CandidatesViewHolder extends RecyclerView.ViewHolder{

    public TextView candidates_ID, candidates_name, candidates_partyList, candidates_yearLevel, candidates_section, candidates_positionRunningFor;
    public CircleImageView candidates_profile;

    public CircleImageView president_profile;
    public TextView president_partylist, president_name, president_votes;

    public CircleImageView vice_president_profile;
    public TextView vice_president_partylist, vice_president_name, vice_president_votes;

    public CircleImageView secretary_profile;
    public TextView secretary_partylist, secretary_name, secretary_votes;

    public CircleImageView auditor_profile;
    public TextView auditor_partylist, auditor_name, auditor_votes;

    public CircleImageView treasurer_profile;
    public TextView treasurer_partylist, treasurer_name, treasurer_votes;

    public CircleImageView p_i_o_profile;
    public TextView p_i_o_partylist, p_i_o_name, p_i_o_votes;

    public CircleImageView peace_officer_profile;
    public TextView peace_officer_partylist, peace_officer_name, peace_officer_votes;

    public CircleImageView grade9_profile;
    public TextView grade9_partylist, grade9_name, grade9_votes;

    public CircleImageView grade10_profile;
    public TextView grade10_partylist, grade10_name, grade10_votes;

    public CircleImageView second_year_profile;
    public TextView second_year_partylist, second_year_name, second_year_votes;

    public CircleImageView fourth_year_profile;
    public TextView fourth_year_partylist, fourth_year_name, fourth_year_votes;

    public CircleImageView home_president_profile;
    public TextView home_president_partylist, home_president_name, home_president_votes;

    public CircleImageView home_vice_president_profile;
    public TextView home_vice_president_partylist, home_vice_president_name, home_vice_president_votes;

    public CircleImageView ballotPresidentProfile;
    public TextView ballotPresidentPartyList, ballotPresidentName;
    public RadioButton presidentRadioBtn;

    public CircleImageView ballotVicePresidentProfile;
    public TextView ballotVicePresidentPartyList, ballotVicePresidentName;
    public RadioButton vicePresidentRadioBtn;

    public CircleImageView ballotSecretaryProfile;
    public TextView ballotSecretaryPartyList, ballotSecretaryName;
    public RadioButton secretaryRadioBtn;

    public CircleImageView ballotAuditorProfile;
    public TextView ballotAuditorPartyList, ballotAuditorName;
    public RadioButton auditorRadioBtn;

    public CircleImageView ballotTreasurerProfile;
    public TextView ballotTreasurerPartyList, ballotTreasurerName;
    public RadioButton treasurerRadioBtn;

    public CircleImageView ballotP_i_oProfile;
    public TextView ballotP_i_oPartyList, ballotP_i_oName;
    public RadioButton p_i_oRadioBtn;

    public CircleImageView ballotPeaceOfficerProfile;
    public TextView ballotPeaceOfficerPartyList, ballotPeaceOfficerName;
    public RadioButton peaceOfficerRadioBtn;

    public CircleImageView ballotGrade9Profile;
    public TextView ballotGrade9PartyList, ballotGrade9Name;
    public RadioButton grade9RadioBtn;

    public CircleImageView ballotGrade10Profile;
    public TextView ballotGrade10PartyList, ballotGrade10Name;
    public RadioButton grade10RadioBtn;

    public CircleImageView ballotSecondYearRepresentativeProfile;
    public TextView ballotSecondYearRepresentativePartyList, ballotSecondYearRepresentativeName;
    public RadioButton secondYearRadioBtn;

    public CircleImageView ballotFourthYearRepresentativeProfile;
    public TextView ballotFourthYearRepresentativePartyList, ballotFourthYearRepresentativeName;
    public RadioButton fourthYearRadioBtn;

    public TextView candidatesPosition;

    public CandidatesViewHolder(@NonNull View itemView) {
        super(itemView);

        ballotPresidentProfile = itemView.findViewById(R.id.ballotPresidentProfile);
        ballotPresidentPartyList = itemView.findViewById(R.id.ballotPresidentPartyList);
        ballotPresidentName = itemView.findViewById(R.id.ballotPresidentName);
        presidentRadioBtn = itemView.findViewById(R.id.presidentRadioButton);

        ballotVicePresidentProfile = itemView.findViewById(R.id.ballotVicePresidentProfile);
        ballotVicePresidentPartyList = itemView.findViewById(R.id.ballotVicePresidentPartyList);
        ballotVicePresidentName = itemView.findViewById(R.id.ballotVicePresidentName);
        vicePresidentRadioBtn = itemView.findViewById(R.id.vicePresidentRadioButton);

        ballotSecretaryProfile = itemView.findViewById(R.id.ballotSecretaryProfile);
        ballotSecretaryPartyList = itemView.findViewById(R.id.ballotSecretaryPartyList);
        ballotSecretaryName  = itemView.findViewById(R.id.ballotSecretaryName);
        secretaryRadioBtn = itemView.findViewById(R.id.secretaryRadioButton);

        ballotAuditorProfile = itemView.findViewById(R.id.ballotAuditorProfile);
        ballotAuditorPartyList = itemView.findViewById(R.id.ballotAuditorPartyList);
        ballotAuditorName = itemView.findViewById(R.id.ballotAuditorName);
        auditorRadioBtn = itemView.findViewById(R.id.auditorRadioButton);

        ballotTreasurerProfile = itemView.findViewById(R.id.ballotTreasurerProfile);
        ballotTreasurerPartyList = itemView.findViewById(R.id.ballotTreasurerPartyList);
        ballotTreasurerName = itemView.findViewById(R.id.ballotTreasurerName);
        treasurerRadioBtn = itemView.findViewById(R.id.treasurerRadioButton);

        ballotP_i_oProfile = itemView.findViewById(R.id.ballotP_i_oProfile);
        ballotP_i_oPartyList = itemView.findViewById(R.id.ballotP_i_oPartyList);
        ballotP_i_oName = itemView.findViewById(R.id.ballotP_i_oName);
        p_i_oRadioBtn = itemView.findViewById(R.id.p_i_oRadioButton);

        ballotPeaceOfficerProfile = itemView.findViewById(R.id.ballotPeaceOfficerProfile);
        ballotPeaceOfficerPartyList = itemView.findViewById(R.id.ballotPeaceOfficerPartyList);
        ballotPeaceOfficerName = itemView.findViewById(R.id.ballotPeaceOfficerName);
        peaceOfficerRadioBtn = itemView.findViewById(R.id.peaceOfficerRadioButton);

        ballotGrade9Profile = itemView.findViewById(R.id.ballotGrade9ChairpersonProfile);
        ballotGrade9PartyList = itemView.findViewById(R.id.ballotGrade9ChairpersonPartyList);
        ballotGrade9Name = itemView.findViewById(R.id.ballotGrade9ChairpersonName);
        grade9RadioBtn = itemView.findViewById(R.id.grade9ChairpersonOfficerRadioButton);

        ballotGrade10Profile = itemView.findViewById(R.id.ballotGrade10ChairpersonProfile);
        ballotGrade10PartyList = itemView.findViewById(R.id.ballotGrade10ChairpersonPartyList);
        ballotGrade10Name = itemView.findViewById(R.id.ballotGrade10ChairpersonName);
        grade10RadioBtn = itemView.findViewById(R.id.grade10ChairpersonOfficerRadioButton);

        ballotSecondYearRepresentativeProfile = itemView.findViewById(R.id.ballotSecondYearRepresentativeProfile);
        ballotSecondYearRepresentativePartyList = itemView.findViewById(R.id.ballotSecondYearRepresentativePartyList);
        ballotSecondYearRepresentativeName = itemView.findViewById(R.id.ballotSecondYearRepresentativeName);
        secondYearRadioBtn = itemView.findViewById(R.id.secondYearRepresentativeOfficerRadioButton);

        ballotFourthYearRepresentativeProfile = itemView.findViewById(R.id.ballotFourthYearRepresentativeProfile);
        ballotFourthYearRepresentativePartyList = itemView.findViewById(R.id.ballotFourthYearRepresentativePartyList);
        ballotFourthYearRepresentativeName = itemView.findViewById(R.id.ballotFourthYearRepresentativeName);
        fourthYearRadioBtn = itemView.findViewById(R.id.fourthYearRepresentativeOfficerRadioButton);

        candidates_profile = itemView.findViewById(R.id.candidatesProfile);
        candidates_ID = itemView.findViewById(R.id.candidatesIDOrLRN);
        candidates_name = itemView.findViewById(R.id.candidatesName);
        candidates_partyList = itemView.findViewById(R.id.candidatesPartyList);
        candidates_yearLevel = itemView.findViewById(R.id.candidatesYearLevel);
        candidates_section = itemView.findViewById(R.id.candidatesSection);
        candidates_positionRunningFor = itemView.findViewById(R.id.candidatesPositionRunningFor);

        president_profile = itemView.findViewById(R.id.presidentProfile);
        president_partylist = itemView.findViewById(R.id.presidentPartylist);
        president_name = itemView.findViewById(R.id.presidentName);
        president_votes = itemView.findViewById(R.id.presidentVotes);

        vice_president_profile = itemView.findViewById(R.id.vicePresidentProfile);
        vice_president_partylist = itemView.findViewById(R.id.vicePresidentPartylist);
        vice_president_name = itemView.findViewById(R.id.vicePresidentName);
        vice_president_votes = itemView.findViewById(R.id.vicePresidentVotes);

        home_president_profile = itemView.findViewById(R.id.homePresidentProfile);
        home_president_partylist = itemView.findViewById(R.id.homePresidentPartylist);
        home_president_name = itemView.findViewById(R.id.homePresidentName);
        home_president_votes = itemView.findViewById(R.id.homePresidentVotes);

        home_vice_president_profile = itemView.findViewById(R.id.homeVicePresidentProfile);
        home_vice_president_partylist = itemView.findViewById(R.id.homeVicePresidentPartylist);
        home_vice_president_name = itemView.findViewById(R.id.homeVicePresidentName);
        home_vice_president_votes = itemView.findViewById(R.id.homeVicePresidentVotes);

        secretary_profile = itemView.findViewById(R.id.secretaryProfile);
        secretary_partylist = itemView.findViewById(R.id.secretaryPartylist);
        secretary_name = itemView.findViewById(R.id.secretaryName);
        secretary_votes = itemView.findViewById(R.id.secretaryVotes);

        auditor_profile = itemView.findViewById(R.id.auditorProfile);
        auditor_partylist = itemView.findViewById(R.id.auditorPartylist);
        auditor_name = itemView.findViewById(R.id.auditorName);
        auditor_votes = itemView.findViewById(R.id.auditorVotes);

        treasurer_profile = itemView.findViewById(R.id.treasurerProfile);
        treasurer_partylist = itemView.findViewById(R.id.treasurerPartylist);
        treasurer_name = itemView.findViewById(R.id.treasurerName);
        treasurer_votes = itemView.findViewById(R.id.treasurerVotes);

        p_i_o_profile = itemView.findViewById(R.id.p_i_oProfile);
        p_i_o_partylist = itemView.findViewById(R.id.p_i_oPartylist);
        p_i_o_name = itemView.findViewById(R.id.p_i_oName);
        p_i_o_votes = itemView.findViewById(R.id.p_i_oVotes);

        peace_officer_profile = itemView.findViewById(R.id.peaceOfficerProfile);
        peace_officer_partylist = itemView.findViewById(R.id.peaceOfficerPartylist);
        peace_officer_name = itemView.findViewById(R.id.peaceOfficerName);
        peace_officer_votes = itemView.findViewById(R.id.peaceOfficerVotes);

        grade9_profile = itemView.findViewById(R.id.grade9Profile);
        grade9_partylist = itemView.findViewById(R.id.grade9Partylist);
        grade9_name = itemView.findViewById(R.id.grade9Name);
        grade9_votes = itemView.findViewById(R.id.grade9Votes);

        grade10_profile = itemView.findViewById(R.id.grade10Profile);
        grade10_partylist = itemView.findViewById(R.id.grade10Partylist);
        grade10_name = itemView.findViewById(R.id.grade10Name);
        grade10_votes = itemView.findViewById(R.id.grade10Votes);

        second_year_profile = itemView.findViewById(R.id.secondYearProfile);
        second_year_partylist = itemView.findViewById(R.id.secondYearPartylist);
        second_year_name = itemView.findViewById(R.id.secondYearName);
        second_year_votes = itemView.findViewById(R.id.secondYearVotes);

        fourth_year_profile = itemView.findViewById(R.id.fourthYearProfile);
        fourth_year_partylist = itemView.findViewById(R.id.fourthYearPartylist);
        fourth_year_name = itemView.findViewById(R.id.fourthYearName);
        fourth_year_votes = itemView.findViewById(R.id.fourthYearVotes);

        candidatesPosition = itemView.findViewById(R.id.position);



    }
}
