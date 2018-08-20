package by.paranoidandroid.movielookup.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import by.paranoidandroid.movielookup.R;
import by.paranoidandroid.movielookup.adapters.MoviesAdapter;
import by.paranoidandroid.movielookup.common.MyApplication;
import by.paranoidandroid.movielookup.model.entities.Movie;
import by.paranoidandroid.movielookup.model.repositories.MoviesRepository;
import by.paranoidandroid.movielookup.utils.GridDividerItemDecoration;

public class MainActivity extends AppCompatActivity {
    private final String BUNDLE_MOVIES = "MOVIES", BUNDLE_ERROR_MSG = "ERROR_MSG",
            BUNDLE_BTN_NEXT = "BTN_NEXT", BUNDLE_BTN_LAST = "BTN_LAST";
    private boolean isBtnNext, isBtnLast;
    private List<Movie> movies;
    private EditText etTitle;
    private ImageButton imageBtn;
    private TextView tvErrorMsg;
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            imageBtn.setEnabled(true);
            imageBtn.setBackgroundColor(getColor(R.color.colorPrimary));
            imageBtn.setImageDrawable(getDrawable(R.drawable.search_black));
            isBtnNext = isBtnLast = false;
            MyApplication.startPage();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.edt_input);
        recyclerView = findViewById(R.id.recycler_view);
        tvErrorMsg = findViewById(R.id.tv_error_msg);
        imageBtn = findViewById(R.id.btn_lookup);

        imageBtn.setOnClickListener(view -> onLookupButtonClick());

        if (savedInstanceState != null) {
            String error = savedInstanceState.getString(BUNDLE_ERROR_MSG, "");
            if (error.isEmpty()) {
                movies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES);
            } else {
                showError(error);
            }
            if (isBtnNext = savedInstanceState.getBoolean(BUNDLE_BTN_NEXT)) {
                imageBtn.setImageDrawable(getResources().getDrawable(R.drawable.navigate_next_black));
            }
            if (isBtnLast = savedInstanceState.getBoolean(BUNDLE_BTN_LAST)) {
                disableButton(imageBtn);
            }
        }

        adapter = new MoviesAdapter(this);
        recyclerView.setAdapter(adapter);
        adjustRecyclerView(getResources().getConfiguration());
    }

    @Override
    protected void onResume() {
        super.onResume();
        etTitle.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onPause() {
        etTitle.removeTextChangedListener(textWatcher);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (tvErrorMsg.getVisibility() == View.VISIBLE) {
            outState.putString(BUNDLE_ERROR_MSG, tvErrorMsg.getText().toString());
        } else {
            if (movies != null) {
                outState.putParcelableArrayList(BUNDLE_MOVIES, (ArrayList<Movie>) movies);
            }
        }
        outState.putBoolean(BUNDLE_BTN_NEXT, isBtnNext);
        outState.putBoolean(BUNDLE_BTN_LAST, isBtnLast);
        super.onSaveInstanceState(outState);
    }

    private void onLookupButtonClick() {
        hideKeyboard();
        sendGetMoviesRq();
    }

    private void sendGetMoviesRq() {
        MoviesRepository moviesRepository = new MoviesRepository();
        moviesRepository.getMovies(etTitle.getText().toString(),
                movies -> onGetMoviesResponse(movies, moviesRepository.isLastPage),
                () -> onGetMoviesFailure(moviesRepository.errorRs, moviesRepository.errorCode));
    }

    private void onGetMoviesResponse(List<Movie> data, boolean isLastPage) {
        movies = data;
        adapter.setItems(movies);
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        tvErrorMsg.setVisibility(View.GONE);
        if (isLastPage) {
            disableButton(imageBtn);
        } else {
            imageBtn.setImageDrawable(getResources().getDrawable(R.drawable.navigate_next_black));
            isBtnNext = true;
        }
    }

    private void onGetMoviesFailure(String error, int errorCode) {
        if (error != null) {
            showError(error);
        } else if (errorCode != 0) {
            showError(getString(R.string.error_code, errorCode));
        } else {
            showError(getString(R.string.smth_wrong));
        }
    }

    private void adjustRecyclerView(Configuration newConfig) {
        final int SPAN_COUNT_PORTRAIT = 2, SPAN_COUNT_HORIZONTAL = 3;
        int spanCount;
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = SPAN_COUNT_PORTRAIT;
        } else {
            spanCount = SPAN_COUNT_HORIZONTAL;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(getApplicationContext(), spanCount, 8));
        if (movies != null) {
            adapter.setItems(movies);
            adapter.notifyDataSetChanged();
        }
    }

    private void showError(String message) {
        if (movies != null) movies.clear();
        tvErrorMsg.setText(message);
        tvErrorMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        disableButton(imageBtn);
    }

    private void disableButton(ImageButton imageBtn) {
        imageBtn.setEnabled(false);
        imageBtn.setBackgroundColor(getColor(R.color.gray));
        isBtnLast = true;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}