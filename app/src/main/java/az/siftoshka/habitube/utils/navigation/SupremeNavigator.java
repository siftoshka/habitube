package az.siftoshka.habitube.utils.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.android.support.FragmentParams;
import ru.terrakok.cicerone.android.support.SupportAppScreen;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.BackTo;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;

public class SupremeNavigator implements Navigator {

    protected final Activity activity;
    protected final FragmentManager fragmentManager;
    protected final int containerId;
    protected LinkedList<String> localStackCopy;

    public SupremeNavigator(@NotNull FragmentActivity activity, int containerId) {
        this(activity, activity.getSupportFragmentManager(), containerId);
    }

    public SupremeNavigator(@NotNull FragmentActivity activity, @NotNull FragmentManager fragmentManager, int containerId) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    @Override
    public void applyCommands(@NotNull Command[] commands) {
        fragmentManager.executePendingTransactions();

        //copy stack before apply commands
        copyStackToLocal();

        for (Command command : commands) {
            try {
                applyCommand(command);
            } catch (RuntimeException e) {
                errorOnApplyCommand(command, e);
            }
        }
    }

    private void copyStackToLocal() {
        localStackCopy = new LinkedList<>();

        final int stackSize = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < stackSize; i++) {
            localStackCopy.add(fragmentManager.getBackStackEntryAt(i).getName());
        }
    }

    protected void applyCommand(@NotNull Command command) {
        if (command instanceof Forward) {
            activityForward((Forward) command);
        } else if (command instanceof Replace) {
            activityReplace((Replace) command);
        } else if (command instanceof BackTo) {
            backTo((BackTo) command);
        } else if (command instanceof Back) {
            fragmentBack();
        }
    }


    protected void activityForward(@NotNull Forward command) {
        SupportAppScreen screen = (SupportAppScreen) command.getScreen();
        Intent activityIntent = screen.getActivityIntent(activity);

        // Start activity
        if (activityIntent != null) {
            Bundle options = createStartActivityOptions(command, activityIntent);
            checkAndStartActivity(screen, activityIntent, options);
        } else {
            fragmentForward(command);
        }
    }

    protected void fragmentForward(@NotNull Forward command) {
        SupportAppScreen screen = (SupportAppScreen) command.getScreen();

        FragmentParams fragmentParams = screen.getFragmentParams();
        Fragment fragment = fragmentParams == null ? createFragment(screen) : null;

        forwardFragmentInternal(command, screen, fragmentParams, fragment);
    }

    protected void fragmentBack() {
        if (localStackCopy.size() > 0) {
            fragmentManager.popBackStack();
            localStackCopy.removeLast();
        } else {
            activityBack();
        }
    }

    protected void activityBack() {
        activity.finish();
    }

    protected void activityReplace(@NotNull Replace command) {
        SupportAppScreen screen = (SupportAppScreen) command.getScreen();
        Intent activityIntent = screen.getActivityIntent(activity);

        // Replace activity
        if (activityIntent != null) {
            Bundle options = createStartActivityOptions(command, activityIntent);
            checkAndStartActivity(screen, activityIntent, options);
            activity.finish();
        } else {
            fragmentReplace(command);
        }
    }

    protected void fragmentReplace(@NotNull Replace command) {
        SupportAppScreen screen = (SupportAppScreen) command.getScreen();

        FragmentParams fragmentParams = screen.getFragmentParams();
        Fragment fragment = fragmentParams == null ? createFragment(screen) : null;

        if (localStackCopy.size() > 0) {
            fragmentManager.popBackStack();
            localStackCopy.removeLast();

            forwardFragmentInternal(command, screen, fragmentParams, fragment);

        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            setupFragmentTransaction(
                    command,
                    fragmentManager.findFragmentById(containerId),
                    fragment,
                    fragmentTransaction
            );

            addFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment);

            fragmentTransaction.commit();
        }
    }

    private void forwardFragmentInternal(
            @NotNull Command command,
            @NotNull SupportAppScreen screen,
            @Nullable FragmentParams fragmentParams,
            @Nullable Fragment fragment
    ) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        setupFragmentTransaction(
                command,
                fragmentManager.findFragmentById(containerId),
                fragment,
                fragmentTransaction
        );

        addFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment);

        fragmentTransaction
                .addToBackStack(screen.getScreenKey())
                .commit();

        localStackCopy.add(screen.getScreenKey());
    }

    private void addFragmentInternal(
            @NotNull FragmentTransaction transaction,
            @NotNull SupportAppScreen screen,
            @Nullable FragmentParams params,
            @Nullable Fragment fragment
    ) {
        if (params != null) {
            transaction.add(
                    containerId,
                    params.getFragmentClass().cast(new Fragment()),
                    params.getArguments().toString()
            );
        } else if (fragment != null) {
            transaction.add(containerId, fragment);
        } else {
            throw new IllegalArgumentException("Either 'params' or 'fragment' shouldn't " +
                    "be null for " + screen.getScreenKey());
        }
    }

    protected void backTo(@NotNull BackTo command) {
        if (command.getScreen() == null) {
            backToRoot();
        } else {
            String key = command.getScreen().getScreenKey();
            int index = localStackCopy.indexOf(key);
            int size = localStackCopy.size();

            if (index != -1) {
                for (int i = 1; i < size - index; i++) {
                    localStackCopy.removeLast();
                }
                fragmentManager.popBackStack(key, 0);
            } else {
                backToUnexisting((SupportAppScreen) command.getScreen());
            }
        }
    }

    private void backToRoot() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        localStackCopy.clear();
    }

    protected void setupFragmentTransaction(@NotNull Command command,
                                            @Nullable Fragment currentFragment,
                                            @Nullable Fragment nextFragment,
                                            @NotNull FragmentTransaction fragmentTransaction) {
    }

    @Nullable
    protected Bundle createStartActivityOptions(@NotNull Command command, @NotNull Intent activityIntent) {
        return null;
    }

    private void checkAndStartActivity(@NotNull SupportAppScreen screen, @NotNull Intent activityIntent, @Nullable Bundle options) {
        // Check if we can start activity
        if (activityIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(activityIntent, options);
        } else {
            unexistingActivity(screen, activityIntent);
        }
    }

    protected void unexistingActivity(@NotNull SupportAppScreen screen, @NotNull Intent activityIntent) {
        // Do nothing by default
    }

    @Nullable
    protected Fragment createFragment(@NotNull SupportAppScreen screen) {
        Fragment fragment = screen.getFragment();

        if (fragment == null) {
            errorWhileCreatingScreen(screen);
            throw new RuntimeException("Can't create a screen: " + screen.getScreenKey());
        }
        return fragment;
    }

    protected void backToUnexisting(@NotNull SupportAppScreen screen) {
        backToRoot();
    }

    protected void errorWhileCreatingScreen(@NotNull SupportAppScreen screen) {
        // Do nothing by default
    }

    protected void errorOnApplyCommand(
            @NotNull Command command,
            @NotNull RuntimeException error
    ) {
        throw error;
    }
}
