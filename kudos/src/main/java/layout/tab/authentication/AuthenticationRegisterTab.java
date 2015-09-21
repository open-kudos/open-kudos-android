package layout.tab.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import open.kudos.R;

/**
 * Created by chc on 15.8.19.
 */
public class AuthenticationRegisterTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.authentication_register,container,false);
        return v;
    }
}
