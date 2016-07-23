package kr.prev.ndnd.controller;

import android.app.Activity;
import android.widget.TextView;
import com.facebook.login.widget.ProfilePictureView;
import kr.prev.ndnd.R;
import kr.prev.ndnd.data.SummarayData;
import kr.prev.ndnd.data.UserData;

public class UserDataViewController implements IViewController {
    UserData userData;

    Activity activity;
    TextView nameText;
    ProfilePictureView profilePicture;

    public UserDataViewController(Activity activity) {
        this.activity = activity;
        this.nameText = (TextView) activity.findViewById(R.id.drawerMameText);
        this.profilePicture = (ProfilePictureView) activity.findViewById(R.id.drawerProfilePicture);

    }

    public void setData(UserData data) { this.userData = data; }
    public UserData getData() { return this.userData; }

    @Override
    public void update() {
        if (userData == null) return;

		nameText.setText( userData.userName );
        profilePicture.setProfileId( userData.socialUid );
    }
}
