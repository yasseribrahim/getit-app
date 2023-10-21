package com.getit.app.ui.activities;

import com.getit.app.persenters.user.UsersCallback;

public class StudentsActivity extends BaseActivity implements UsersCallback/*, StudentsAdapter.OnItemClickListener*/ {
//    private FragmentStudentsBinding binding;
//    private UsersPresenter presenter;
//    private StudentsAdapter usersAdapter;
//    private List<User> users, searchedUsers;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentStudentsBinding.inflate(inflater);
//
//        presenter = new UsersPresenter(this);
//
//        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
//        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                load();
//            }
//        });
//
//        binding.textSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                search(binding.textSearch.getText().toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        users = new ArrayList<>();
//        searchedUsers = new ArrayList<>();
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        usersAdapter = new StudentsAdapter(searchedUsers, this);
//        binding.recyclerView.setAdapter(usersAdapter);
//
//        return binding.getRoot();
//    }
//
//    private void load() {
//        presenter.getUsers(Constants.USER_TYPE_STUDENT);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        load();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (presenter != null) {
//            presenter.onDestroy();
//        }
//    }
//
//    @Override
//    public void onGetUsersComplete(List<User> users) {
//        this.users.clear();
//        this.users.addAll(users);
//        search(binding.textSearch.getText().toString());
//    }
//
//    @Override
//    public void onShowLoading() {
//        binding.refreshLayout.setRefreshing(true);
//    }
//
//    @Override
//    public void onHideLoading() {
//        binding.refreshLayout.setRefreshing(false);
//    }
//
//    @Override
//    public void onFailure(String message, View.OnClickListener listener) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void search(String searchedText) {
//        searchedUsers.clear();
//        if (!searchedText.isEmpty()) {
//            for (User user : users) {
//                if (isMatched(user, searchedText)) {
//                    searchedUsers.add(user);
//                }
//            }
//        } else {
//            searchedUsers.addAll(users);
//        }
//
//        refresh();
//    }
//
//    private boolean isMatched(User user, String text) {
//        String searchedText = text.toLowerCase();
//        boolean result = user.getFullName().toLowerCase().contains(searchedText) ||
//                (user.getAddress() != null && user.getAddress().toLowerCase().contains(searchedText)) ||
//                (user.getPhone() != null && user.getPhone().toLowerCase().contains(searchedText)) ||
//                (user.getUsername() != null && user.getUsername().toLowerCase().contains(searchedText));
//        return result;
//    }
//
//    private void refresh() {
//        binding.message.setVisibility(View.GONE);
//        if (searchedUsers.isEmpty()) {
//            binding.message.setVisibility(View.VISIBLE);
//        }
//
//        usersAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onItemViewListener(User user) {
//
//    }
}