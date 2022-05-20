// Generated by view binder compiler. Do not edit!
package be.kuleuven.timetoclimb.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import be.kuleuven.timetoclimb.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SignupTabFragmentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnSignup;

  @NonNull
  public final EditText confirmPassword;

  @NonNull
  public final EditText signupPassword;

  @NonNull
  public final EditText username;

  private SignupTabFragmentBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnSignup,
      @NonNull EditText confirmPassword, @NonNull EditText signupPassword,
      @NonNull EditText username) {
    this.rootView = rootView;
    this.btnSignup = btnSignup;
    this.confirmPassword = confirmPassword;
    this.signupPassword = signupPassword;
    this.username = username;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SignupTabFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SignupTabFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.signup_tab_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SignupTabFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnSignup;
      Button btnSignup = ViewBindings.findChildViewById(rootView, id);
      if (btnSignup == null) {
        break missingId;
      }

      id = R.id.confirmPassword;
      EditText confirmPassword = ViewBindings.findChildViewById(rootView, id);
      if (confirmPassword == null) {
        break missingId;
      }

      id = R.id.signupPassword;
      EditText signupPassword = ViewBindings.findChildViewById(rootView, id);
      if (signupPassword == null) {
        break missingId;
      }

      id = R.id.username;
      EditText username = ViewBindings.findChildViewById(rootView, id);
      if (username == null) {
        break missingId;
      }

      return new SignupTabFragmentBinding((ConstraintLayout) rootView, btnSignup, confirmPassword,
          signupPassword, username);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}